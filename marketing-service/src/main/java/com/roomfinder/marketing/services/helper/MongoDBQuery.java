package com.roomfinder.marketing.services.helper;
import com.roomfinder.marketing.controllers.dto.request.FilterRequest;
import com.roomfinder.marketing.controllers.dto.request.SearchPostRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Component
public class MongoDBQuery {
    private static final String PROPERTIES = "pricingDetails.basePrice";

    public Query buildSearchQuery(SearchPostRequest searchRequest) {
        Query query = new Query();

        if (searchRequest.getTitle() != null && !searchRequest.getTitle().isEmpty()) {
            query.addCriteria(Criteria.where("title").regex(searchRequest.getTitle(), "i"));
        }
        if (searchRequest.getType() != null && !searchRequest.getType().isEmpty()) {
            query.addCriteria(Criteria.where("roomInfo.type").regex(searchRequest.getType(), "i"));
        }

        if (searchRequest.getDescription() != null && !searchRequest.getDescription().isEmpty()) {
            query.addCriteria(Criteria.where("description").regex(searchRequest.getDescription(), "i"));
        }
        if (searchRequest.getName() != null && !searchRequest.getName().isEmpty()) {
            query.addCriteria(Criteria.where("roomInfo.name").regex(searchRequest.getName(), "i"));
        }
        if (searchRequest.getAddress() != null && !searchRequest.getAddress().isEmpty()) {
            query.addCriteria(Criteria.where("roomInfo.address").regex(searchRequest.getAddress(), "i"));
        }
        if (searchRequest.getBasePrice() != null && !searchRequest.getBasePrice().isEmpty()) {
            try {
                BigDecimal basePrice = new BigDecimal(searchRequest.getBasePrice());

                BigDecimal lowerBound = basePrice.multiply(new BigDecimal("0.8"));
                BigDecimal upperBound = basePrice.multiply(new BigDecimal("1.2"));

                query.addCriteria(Criteria.where(PROPERTIES)
                        .gte(lowerBound)
                        .lte(upperBound));
            } catch (NumberFormatException e) {
                log.error("Invalid base price format: {}", searchRequest.getBasePrice());
            }
        }
        return query;
    }

    public Query buildDistrictQuery(int district) {
        Query query = new Query();
        if (district > 0) {
            query.addCriteria(Criteria.where("roomInfo.district").is(district));  // So sánh với int, không phải String
        }
        return query;
    }


    public List<AggregationOperation> buildCountOperations(FilterRequest request) {
        List<AggregationOperation> operations = new ArrayList<>();
        operations.add(LookupOperation.newLookup()
                .from("promotionalRoom")
                .localField("roomId")
                .foreignField("roomId")
                .as("roomDetails"));
        operations.add(Aggregation.unwind("roomDetails", true));

        if (request.getMinPrice() != null && request.getMaxPrice() != null) {
            operations.add(Aggregation.match(Criteria.where(PROPERTIES)
                    .gte(request.getMinPrice())
                    .lte(request.getMaxPrice())));
        }

        // Xử lý tìm kiếm địa chỉ (district và ward)
        if (hasAddressFilter(request)) {
            operations.add(buildAddressFilter(request));
        }

        if (request.getType() >0) {
            operations.add(Aggregation.match(Criteria.where("roomInfo.type").is(request.getType())));
        }

        if (request.getHasPromotion() != null) {
            operations.add(Aggregation.match(Boolean.TRUE.equals(request.getHasPromotion()) ?
                    Criteria.where("roomDetails.fixPrice").ne(null) :
                    Criteria.where("roomDetails.fixPrice").isNull()));
        }

        return operations;
    }

    public boolean hasAddressFilter(FilterRequest request) {
        return (request.getDistrict() >0 ) ||
                (request.getCommune() >0 );
    }

    public AggregationOperation buildAddressFilter(FilterRequest request) {
        Criteria criteria = new Criteria();
        // Thêm điều kiện cho district nếu district > 0
        if (request.getDistrict() > 0) {
            criteria.and("roomInfo.district").is(request.getDistrict());
        }
        // Thêm điều kiện cho commune nếu commune > 0
        if (request.getCommune() > 0) {
            criteria.and("roomInfo.commune").is(request.getCommune());
        }
        // Nếu không có điều kiện nào được áp dụng, trả về null
        if (criteria.getCriteriaObject().isEmpty()) {
            return null;
        }
        return Aggregation.match(criteria);
    }
    public Aggregation buildFilterAggregation(FilterRequest request, PageRequest pageable) {
        List<AggregationOperation> operations = new ArrayList<>();

        // Lookup promotional rooms
        operations.add(Aggregation.lookup(
                "promotionalRoom",   // Collection name
                "roomId",            // Field in roomSalePosts to match
                "roomId",            // Field in promotionalRoom to match
                "promotionalInfo"    // Output array field
        ));

        // Unwind promotional info (optional, depends on your use case)
        operations.add(Aggregation.unwind("promotionalInfo", true));

        // Basic status filtering
        Criteria baseCriteria = Criteria.where("status").nin("EXPIRED", "PENDING", "REJECTED")
                .and("statusShow").is("ACTIVE");
        operations.add(Aggregation.match(baseCriteria));

        // District filtering
        if (request.getDistrict() > 0) {
            operations.add(Aggregation.match(
                    Criteria.where("roomInfo.district").is(request.getDistrict())
            ));
        }

        // Commune filtering
        if (request.getCommune() > 0) {
            operations.add(Aggregation.match(
                    Criteria.where("roomInfo.commune").is(request.getCommune())
            ));
        }

        // Type filtering
        if (request.getType() >0) {
            operations.add(Aggregation.match(
                    Criteria.where("roomInfo.type").is(request.getType())
            ));
        }

        // Promotion filtering with advanced logic
        if (request.getHasPromotion() != null) {
            Criteria promotionCriteria = new Criteria();
            if (Boolean.TRUE.equals(request.getHasPromotion())) {
                // Room has a promotional record with either fixed price or percentage discount
                promotionCriteria.orOperator(
                        Criteria.where("promotionalInfo.fixPrice").exists(true),
                        Criteria.where("promotionalInfo.percent").gt(0)
                );
            } else {
                // No promotional record or no active promotions
                promotionCriteria.andOperator(
                        Criteria.where("promotionalInfo").is(null)
                                .orOperator(
                                        Criteria.where("promotionalInfo.fixPrice").exists(false),
                                        Criteria.where("promotionalInfo.percent").lte(0)
                                )
                );
            }
            operations.add(Aggregation.match(promotionCriteria));
        }

        // Price filtering with promotional price consideration
        if (request.getMinPrice() != null || request.getMaxPrice() != null) {
            Criteria priceCriteria = new Criteria();

            // Price range filtering
            if (request.getMinPrice() != null) {
                priceCriteria.and("finalPrice").gte(request.getMinPrice());
            }
            if (request.getMaxPrice() != null) {
                priceCriteria.and("finalPrice").lte(request.getMaxPrice());
            }
            operations.add(Aggregation.match(priceCriteria));
        }

        // Sorting logic with multiple criteria
        List<Sort.Order> sortOrders = new ArrayList<>();

        // Always prioritize index
        sortOrders.add(Sort.Order.asc("index"));

        // Price sorting with promotional consideration
        if (request.getSortByPrice() != null) {
            Sort.Direction priceDirection = "ASC".equalsIgnoreCase(request.getSortByPrice())
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC;

            sortOrders.add(
                    priceDirection == Sort.Direction.ASC
                            ? Sort.Order.asc("finalPrice")
                            : Sort.Order.desc("finalPrice")
            );
        }

        // Created date sorting
        if (request.getSortByCreated() != null) {
            Sort.Direction createdDirection = "ASC".equalsIgnoreCase(request.getSortByCreated())
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC;

            sortOrders.add(
                    createdDirection == Sort.Direction.ASC
                            ? Sort.Order.asc("createdDate")
                            : Sort.Order.desc("createdDate")
            );
        }

        // Apply complex sorting
        operations.add(Aggregation.sort(Sort.by(sortOrders)));

        // Pagination
        operations.add(Aggregation.skip(pageable.getOffset()));
        operations.add(Aggregation.limit(pageable.getPageSize()));

        return Aggregation.newAggregation(operations);
    }

}
