package com.roomfinder.marketing.services.impl;

import com.roomfinder.marketing.controllers.dto.PageResponse;
import com.roomfinder.marketing.controllers.dto.request.FilterRequest;
import com.roomfinder.marketing.controllers.dto.request.RoomSalePostRequest;
import com.roomfinder.marketing.controllers.dto.request.SearchPostRequest;
import com.roomfinder.marketing.controllers.dto.response.InfoMarketing;
import com.roomfinder.marketing.controllers.dto.response.RoomSalePostResponse;
import com.roomfinder.marketing.exception.AppException;
import com.roomfinder.marketing.exception.ErrorCode;
import com.roomfinder.marketing.mappers.MarketingMapper;
import com.roomfinder.marketing.repositories.*;
import com.roomfinder.marketing.repositories.clients.dto.InfoUserForCount;
import com.roomfinder.marketing.repositories.entities.FeaturedRoomEntity;
import com.roomfinder.marketing.repositories.entities.PromotionalRoomEntity;
import com.roomfinder.marketing.repositories.entities.RoomSalePostEntity;
import com.roomfinder.marketing.services.MarketingService;
import com.roomfinder.marketing.services.helper.GetTimeExpiry;
import com.roomfinder.marketing.services.helper.MongoDBQuery;
import com.roomfinder.marketing.utility.DateTimeFormatter;
import com.roomfinder.marketing.utility.RoomSalePostRequestValidator;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.roomfinder.marketing.constants.Status.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MarketingServiceImpl implements MarketingService {

    MarketingMapper marketingMapper;
    RoomSalePostRepository roomSalePostRepository;
    MongoTemplate mongoTemplate;
    FirebaseStorageClient firebaseStorageClient;
    FeaturedRepository featuredRepository;
    PromotionalRepository promotionalRepository;
    DateTimeFormatter dateTimeFormatter;
    UserRepository userRepository;
    MongoDBQuery mongoDBQuery;
    PaymentRepository paymentRepository;
    GetTimeExpiry getTimeExpiry;
    private static final Random RANDOM = new Random();


    public static String generateRoomId() {
        int randomNum = RANDOM.nextInt(999) + 1;
        return String.format("room%03d", randomNum);
    }


    @Override
//    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public RoomSalePostResponse createPost(RoomSalePostRequest request) {
        // Lấy thông tin user hiện tại
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        int userId = Integer.parseInt(jwt.getClaim("userId").toString());
        // Tạo bài đăng
        var roomSalePostEntity = marketingMapper.toCreateRoomSalePost(request);
//        if (RoomSalePostRequestValidator.validate(request)) {
//            roomSalePostEntity.setStatus(PENDING.name());
//        }
        roomSalePostEntity.setStatus(ACTIVE.name());
        roomSalePostEntity.setUserId(userId);
        String roomId = generateRoomId();
        roomSalePostEntity.setRoomId(roomId);
        if (Objects.equals(request.getStatusShow(), "Còn phòng")) {
            roomSalePostEntity.setStatusShow(ACTIVE.name());
        }
        if (Objects.equals(request.getStatusShow(), "Đang thi công")) {
            roomSalePostEntity.setStatusShow(PENDING.name());
        }
        if (Objects.equals(request.getStatusShow(), "Hết phòng")) {
            roomSalePostEntity.setStatusShow(REJECTED.name());
        }
        if (request.getTypePackage()==0)
        {
            roomSalePostRepository.save(roomSalePostEntity);
        }
        else {
            roomSalePostRepository.save(roomSalePostEntity);
            if (roomSalePostEntity.getCreatedDate() != null) {
                roomSalePostEntity.setCreated(dateTimeFormatter.format(roomSalePostEntity.getCreatedDate()));
            } else {
                throw new AppException(ErrorCode.POST_CREATION_FAILED);
            }
            paymentRepository.minusBalance(request.getTypePackage(),roomId);
        }
        return marketingMapper.toResponseRoomSalePost(roomSalePostEntity);
    }
    @Override
//    @PreAuthorize("hasRole('ADMIN')")
    public RoomSalePostResponse updatePost(String id, RoomSalePostRequest request) {
        return roomSalePostRepository.findById(id)
                .map(existingPost -> {
                    marketingMapper.updateRoomSalePost(request, existingPost);
                    firebaseStorageClient.updatePostImagesWithSignedUrls(existingPost);
                    roomSalePostRepository.save(existingPost); // Save after updating images
                    return marketingMapper.toResponseRoomSalePost(existingPost);
                })
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));
    }

    @Override
//    @PreAuthorize("hasRole('ADMIN')")
    public void deletePost(String id) {
        roomSalePostRepository.findById(id)
                .ifPresentOrElse(roomSalePostRepository::delete,
                        () -> {
                            throw new AppException(ErrorCode.POST_NOT_FOUND);
                        });
    }

    @Override
    public RoomSalePostResponse getPostById(String id) {
        var roomSalePostEntity = roomSalePostRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        List<PromotionalRoomEntity> promotionalEntities = promotionalRepository.findAll();
        Set<String> promotionalRoomIds = promotionalEntities.stream()
                .map(PromotionalRoomEntity::getRoomId)
                .collect(Collectors.toSet());

        RoomSalePostResponse roomSalePostResponse;
        if (promotionalRoomIds.contains(roomSalePostEntity.getRoomId())) {
            var promotionalRoomEntity = promotionalRepository.findByRoomId(roomSalePostEntity.getRoomId()).orElse(null);
            roomSalePostResponse = marketingMapper.toRoomSalePostPromotionalResponse(promotionalRoomEntity, roomSalePostEntity);
            assert promotionalRoomEntity != null;
            roomSalePostResponse.setFixPrice(promotionalRoomEntity.getFixPrice());
        } else {
            roomSalePostResponse = marketingMapper.toResponseRoomSalePost(roomSalePostEntity);
        }
        roomSalePostResponse.setCreated(dateTimeFormatter.format(roomSalePostEntity.getCreatedDate()));
        return roomSalePostResponse;
    }

    @Override
    public PageResponse<RoomSalePostResponse> getPostByDistrict(int district, int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        Query query = mongoDBQuery.buildDistrictQuery(district);
        long totalElements = mongoTemplate.count(query, RoomSalePostEntity.class);
        query.with(pageable);

        List<RoomSalePostResponse> postResponses = mongoTemplate.find(query, RoomSalePostEntity.class).stream()
                .map(marketingMapper::toResponseRoomSalePost)
                .toList();
        return createPageResponse(page, size, totalElements, postResponses);
    }


    @Override
    public PageResponse<RoomSalePostResponse> getPosts(int page, int size) {
        // List of statuses to exclude
        List<String> excludedStatuses = Arrays.asList("EXPIRED", "PENDING", "REJECTED");

        // Custom query to prioritize non-zero index
        Pageable pageable = PageRequest.of(page - 1, size);

        // Fetch paginated data from database, excluding specified statuses
        Page<RoomSalePostEntity> pageData = roomSalePostRepository.findByStatusNotInOrderByIndexAscCreatedDateDesc(
                excludedStatuses,
                pageable
        );

        // Fetch promotional rooms
        List<PromotionalRoomEntity> promotionalEntities = promotionalRepository.findAll();
        Set<String> promotionalRoomIds = promotionalEntities.stream()
                .map(PromotionalRoomEntity::getRoomId)
                .collect(Collectors.toSet());

        // Transform data from RoomSalePostEntity to RoomSalePostResponse
        var roomSalePostList = pageData.getContent().stream()
                .map(roomSalePostEntity -> {
                    RoomSalePostResponse roomSalePostResponse;
                    if (promotionalRoomIds.contains(roomSalePostEntity.getRoomId())) {
                        var promotionalRoomEntity = promotionalRepository.findByRoomId(roomSalePostEntity.getRoomId()).orElse(null);
                        roomSalePostResponse = marketingMapper.toRoomSalePostPromotionalResponse(promotionalRoomEntity, roomSalePostEntity);
                        assert promotionalRoomEntity != null;
                        roomSalePostResponse.setFixPrice(promotionalRoomEntity.getFixPrice());
                    } else {
                        roomSalePostResponse = marketingMapper.toResponseRoomSalePost(roomSalePostEntity);
                    }
                    roomSalePostResponse.setCreated(dateTimeFormatter.format(roomSalePostEntity.getCreatedDate()));
                    return roomSalePostResponse;
                })
                .toList();

        // Create and return PageResponse object
        return PageResponse.<RoomSalePostResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(roomSalePostList)
                .build();
    }


    @Override
    public PageResponse<RoomSalePostResponse> flitterPostWithStatusForUser(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var user = userRepository.getMyInfo();

        Page<RoomSalePostEntity> pageData;
        if (StringUtils.hasText(status)) {
            pageData = roomSalePostRepository.findAllByUserIdAndStatus(user.getId(), status, pageable);
        } else {
            pageData = roomSalePostRepository.findAllByUserId(user.getId(), pageable);
        }

        // Lấy danh sách promotional rooms
        List<PromotionalRoomEntity> promotionalEntities = promotionalRepository.findAll();
        Set<String> promotionalRoomIds = promotionalEntities.stream()
                .map(PromotionalRoomEntity::getRoomId)
                .collect(Collectors.toSet());

        // Lấy danh sách featured rooms và expiry time
        List<FeaturedRoomEntity> featuredRooms = featuredRepository.findByUserId(user.getId());
        Map<String, Instant> featuredExpiryMap = featuredRooms.stream()
                .collect(Collectors.toMap(
                        FeaturedRoomEntity::getRoomId,
                        FeaturedRoomEntity::getExpiry,
                        (existing, replacement) -> existing // Xử lý trường hợp trùng key
                ));

        List<RoomSalePostResponse> roomSalePostList = pageData.getContent().stream().map(roomSalePostEntity -> {
            RoomSalePostResponse roomSalePostResponse;

            // Xử lý promotional rooms
            if (promotionalRoomIds.contains(roomSalePostEntity.getRoomId())) {
                var promotionalRoomEntity = promotionalRepository.findByRoomId(roomSalePostEntity.getRoomId()).orElse(null);
                roomSalePostResponse = marketingMapper.toRoomSalePostPromotionalResponse(promotionalRoomEntity, roomSalePostEntity);
                assert promotionalRoomEntity != null;
                roomSalePostResponse.setFixPrice(promotionalRoomEntity.getFixPrice());
            } else {
                roomSalePostResponse = marketingMapper.toResponseRoomSalePost(roomSalePostEntity);
            }

            // Thêm thông tin về expiry time nếu là featured room
            Instant expiryTime = featuredExpiryMap.get(roomSalePostEntity.getRoomId());
            if (expiryTime != null) {
                long remainingSeconds = ChronoUnit.SECONDS.between(Instant.now(), expiryTime);
                if (remainingSeconds > 0) {
                    // Đảm bảo set giá trị
                    roomSalePostResponse.setRemainingFeaturedTime(remainingSeconds);
                    roomSalePostResponse.setRemainingFeaturedTimeFormatted(
                            getTimeExpiry.formatRemainingTime(remainingSeconds)
                    );
                }
            }
            log.info("Featured Rooms for user: {}", featuredRooms.size());
            log.info("Featured Expiry Map: {}", featuredExpiryMap);
            roomSalePostResponse.setCreated(dateTimeFormatter.format(roomSalePostEntity.getCreatedDate()));
            return roomSalePostResponse;
        }).collect(Collectors.toList());

        return PageResponse.<RoomSalePostResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(roomSalePostList)
                .build();
    }

    @Override
    public PageResponse<RoomSalePostResponse> filterPostWithType(int type, int page, int size) {
        // Loại bỏ các trạng thái không mong muốn
        List<String> excludedStatuses = Arrays.asList("EXPIRED", "PENDING", "REJECTED");

        // Tạo đối tượng pageable
        Pageable pageable = PageRequest.of(page - 1, size);

        // Tạo Query và thêm các tiêu chí lọc
        Query query = new Query();
        query.addCriteria(Criteria.where("status").nin(excludedStatuses)); // Lọc các status không mong muốn

        // Thêm bộ lọc `type` (nếu được cung cấp)
        if (type > 0) {
            query.addCriteria(Criteria.where("roomInfo.type").is(type));
        }

        // Thêm phân trang
        query.with(pageable);

        // Sử dụng MongoTemplate để lấy dữ liệu
        List<RoomSalePostEntity> roomSalePostEntities = mongoTemplate.find(query, RoomSalePostEntity.class);
        long totalElements = mongoTemplate.count(query.skip(0).limit(0), RoomSalePostEntity.class);

        // Fetch danh sách phòng khuyến mãi
        List<PromotionalRoomEntity> promotionalEntities = promotionalRepository.findAll();
        Set<String> promotionalRoomIds = promotionalEntities.stream()
                .map(PromotionalRoomEntity::getRoomId)
                .collect(Collectors.toSet());

        // Transform data từ RoomSalePostEntity sang RoomSalePostResponse
        var roomSalePostList = roomSalePostEntities.stream()
                .map(roomSalePostEntity -> {
                    RoomSalePostResponse roomSalePostResponse;
                    if (promotionalRoomIds.contains(roomSalePostEntity.getRoomId())) {
                        var promotionalRoomEntity = promotionalRepository.findByRoomId(roomSalePostEntity.getRoomId()).orElse(null);
                        roomSalePostResponse = marketingMapper.toRoomSalePostPromotionalResponse(promotionalRoomEntity, roomSalePostEntity);
                        assert promotionalRoomEntity != null;
                        roomSalePostResponse.setFixPrice(promotionalRoomEntity.getFixPrice());
                    } else {
                        roomSalePostResponse = marketingMapper.toResponseRoomSalePost(roomSalePostEntity);
                    }
                    roomSalePostResponse.setCreated(dateTimeFormatter.format(roomSalePostEntity.getCreatedDate()));
                    return roomSalePostResponse;
                })
                .toList();

        // Tạo và trả về đối tượng PageResponse
        return PageResponse.<RoomSalePostResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages((int) Math.ceil((double) totalElements / size))
                .totalElements(totalElements)
                .data(roomSalePostList)
                .build();
    }

    @Override
    public InfoMarketing getInfoMarketing() {
        // 1. Lấy tất cả userIds từ userRepository thông qua phương thức quantityUser
        InfoUserForCount userIds = userRepository.quantityUser();  // Giả sử quantityUser trả về đối tượng InfoUserForCount

        // 2. Truy xuất danh sách userId từ InfoUserForCount
        List<Integer> userIdList = userIds.getId();  // Lấy danh sách userId từ InfoUserForCount
        Integer quantityUsers = userIds.getQuantityUser(); // Số lượng người dùng

        // 3. Lấy tất cả bài viết của những user đó từ roomSalePostRepository
        List<RoomSalePostEntity> userPosts = roomSalePostRepository.findAll().stream()
                .filter(post -> userIdList.contains(post.getUserId()))  // Lọc bài viết theo userId
                .collect(Collectors.toList());

        // 4. Lọc các userId đã đăng bài
        List<Integer> usersWithPosts = userPosts.stream()
                .map(RoomSalePostEntity::getUserId)  // Lấy userId từ mỗi bài viết
                .distinct()  // Lọc để không bị trùng lặp
                .collect(Collectors.toList());

        // 5. Đếm số lượng user đã đăng bài
        int quantityUsersWithPosts = usersWithPosts.size();

        // 6. Đếm số lượng bài viết có typeSale là 1 hoặc 2
        int quantityPostsTypeSale1 = (int) userPosts.stream()
                .filter(post -> post.getRoomInfo().getTypeSale() == 1)  // Lọc bài viết có typeSale = 1
                .count();

        int quantityPostsTypeSale2 = (int) userPosts.stream()
                .filter(post -> post.getRoomInfo().getTypeSale() == 2)  // Lọc bài viết có typeSale = 2
                .count();

        // 7. Tính tổng số bài viết có typeSale là 1 hoặc 2
        int totalPosts = quantityPostsTypeSale1 + quantityPostsTypeSale2;

        // 8. Xây dựng đối tượng InfoMarketing để trả về
        return InfoMarketing.builder()
                .quantityUser(quantityUsers)  // Số lượng người dùng
                .quantityBroker(quantityUsersWithPosts) // Số lượng người dùng đã đăng bài
                .quantityTypeSale(quantityPostsTypeSale1)  // Số lượng bài viết có typeSale = 1
                .quantityTypeSaleRent(quantityPostsTypeSale2)  // Số lượng bài viết có typeSale = 2
                .totalPosts(totalPosts)
                .build();
    }
    private PageResponse<RoomSalePostResponse> createPageResponse(int page, int size, long totalElements, List<RoomSalePostResponse> postResponses) {
        return PageResponse.<RoomSalePostResponse>builder()
                .currentPage(page)
                .data(postResponses)
                .pageSize(size)
                .totalElements(totalElements)
                .totalPages((int) Math.ceil((double) totalElements / size))
                .build();
    }
    @Override
    public PageResponse<RoomSalePostResponse> getPostsFeatured(int page, int size) {
        // Danh sách trạng thái cần loại bỏ
        List<String> excludedStatuses = Arrays.asList("EXPIRED", "PENDING", "REJECTED");

        // Tạo truy vấn với sắp xếp ưu tiên thời gian (expire) trước, sau đó mới đến index
        Query query = new Query(
                Criteria.where("index").ne(null)
                        .andOperator(Criteria.where("index").gt(0))
        )
                .with(Sort.by(
                        Sort.Order.asc("expiry"),     // Ưu tiên thời gian expire tăng dần (các mục sắp hết hạn sẽ đứng trước)
                        Sort.Order.desc("index")      // Sau đó mới đến index giảm dần
                ))
                .with(PageRequest.of(page - 1, size));

        // Lấy các featured rooms
        List<FeaturedRoomEntity> featuredRooms = mongoTemplate.find(query, FeaturedRoomEntity.class);

        // Lấy các roomSalePosts tương ứng và lọc
        List<RoomSalePostResponse> postResponses = featuredRooms.stream()
                .map(featuredRoomEntity -> {
                    // Lấy danh sách các bài đăng từ roomSalePostRepository
                    List<RoomSalePostEntity> roomSalePosts = roomSalePostRepository.findAllByRoomId(featuredRoomEntity.getRoomId());

                    // Lọc các phòng có status nằm trong excludedStatuses
                    return roomSalePosts.stream()
                            .filter(roomSalePostEntity -> !excludedStatuses.contains(roomSalePostEntity.getStatus()))
                            .map(marketingMapper::toResponseRoomSalePost)
                            .findFirst() // Lấy bài đăng đầu tiên thỏa mãn
                            .orElse(null); // Trả về null nếu không có bài đăng nào
                })
                .filter(Objects::nonNull) // Loại bỏ các giá trị null
                .collect(Collectors.toList());

        // Đếm tổng số featured rooms thỏa mãn điều kiện
        long totalElements = mongoTemplate.count(query.limit(0).skip(0), FeaturedRoomEntity.class);

        // Trả về PageResponse với thông tin của các bài đăng đã sắp xếp
        return PageResponse.<RoomSalePostResponse>builder()
                .currentPage(page)
                .data(postResponses)
                .pageSize(size)
                .totalElements(totalElements)
                .totalPages((int) Math.ceil((double) totalElements / size))
                .build();
    }
//    public PageResponse<RoomSalePostResponse> getPostsFeatured(int page, int size) {
//        // Danh sách trạng thái cần loại bỏ
//        List<String> excludedStatuses = Arrays.asList("EXPIRED", "PENDING", "REJECTED");
//
//        // Tạo Criteria để lọc và sắp xếp
//        Criteria criteria = new Criteria();
//        criteria.andOperator(
//                Criteria.where("index").ne(null),
//                Criteria.where("index").gt(0)
//        );
//
//        // Tạo truy vấn
//        Query query = new Query(criteria)
//                .with(Sort.by(
//                        Sort.Order.desc("index"),
//                        Sort.Order.asc("expiry")
//                ))
//                .with(PageRequest.of(page - 1, size));
//
//        // Lấy các featured rooms
//        List<FeaturedRoomEntity> featuredRooms = mongoTemplate.find(query, FeaturedRoomEntity.class);
//
//        // Lấy các roomSalePosts tương ứng và lọc
//        List<RoomSalePostResponse> postResponses = featuredRooms.stream()
//                .map(featuredRoomEntity -> {
//                    // Lấy danh sách các bài đăng từ roomSalePostRepository
//                    List<RoomSalePostEntity> roomSalePosts = roomSalePostRepository.findAllByRoomId(featuredRoomEntity.getRoomId());
//
//                    // Lọc các phòng có status nằm trong excludedStatuses
//                    return roomSalePosts.stream()
//                            .filter(roomSalePostEntity -> !excludedStatuses.contains(roomSalePostEntity.getStatus()))
//                            .map(marketingMapper::toResponseRoomSalePost)
//                            .findFirst() // Lấy bài đăng đầu tiên thỏa mãn
//                            .orElse(null); // Trả về null nếu không có bài đăng nào
//                })
//                .filter(Objects::nonNull) // Loại bỏ các giá trị null
//                .collect(Collectors.toList());
//
//        // Đếm tổng số featured rooms thỏa mãn điều kiện
//        long totalElements = mongoTemplate.count(query.limit(0).skip(0), FeaturedRoomEntity.class);
//
//        // Trả về PageResponse với thông tin của các bài đăng đã sắp xếp
//        return PageResponse.<RoomSalePostResponse>builder()
//                .currentPage(page)
//                .data(postResponses)
//                .pageSize(size)
//                .totalElements(totalElements)
//                .totalPages((int) Math.ceil((double) totalElements / size))
//                .build();
//    }
//    public PageResponse<RoomSalePostResponse> getPostsFeatured(int page, int size) {
//        // Danh sách trạng thái cần loại bỏ
//        List<String> excludedStatuses = Arrays.asList("EXPIRED", "PENDING", "REJECTED");
//
//        // Sắp xếp theo index của featuredRepository và page
//        Sort featuredSort = Sort.by(Sort.Order.asc("index"));
//        Pageable pageable = PageRequest.of(page - 1, size, featuredSort);
//
//        // Lấy các featured rooms từ featuredRepository
//        Page<FeaturedRoomEntity> featuredRooms = featuredRepository.findAll(pageable);
//
//        // Lấy các roomSalePosts tương ứng và sắp xếp theo index của roomSalePostRepository và createDate
//        List<RoomSalePostResponse> postResponses = featuredRooms.getContent().stream()
//                .filter(entity -> entity.getIndex() != null && entity.getIndex() > 0)
//                .map(featuredRoomEntity -> {
//                    // Lấy danh sách các bài đăng từ roomSalePostRepository theo roomId và sắp xếp theo index và createDate
//                    List<RoomSalePostEntity> roomSalePosts = roomSalePostRepository.findAllByRoomId(featuredRoomEntity.getRoomId()                     );
//
//                    // Lọc các phòng có status nằm trong excludedStatuses
//                    return roomSalePosts.stream()
//                            .filter(roomSalePostEntity -> !excludedStatuses.contains(roomSalePostEntity.getStatus())) // Lọc theo status
//                            .map(marketingMapper::toResponseRoomSalePost)
//                            .toList();
//                })
//                .flatMap(List::stream)
//                .toList();
//
//        // Trả về PageResponse với thông tin của các bài đăng đã sắp xếp
//        return PageResponse.<RoomSalePostResponse>builder()
//                .currentPage(page)
//                .data(postResponses)
//                .pageSize(featuredRooms.getSize())
//                .totalElements(postResponses.size())
//                .totalPages((int) Math.ceil((double) postResponses.size() / size))
//                .build();
//    }


    @Override
    public PageResponse<RoomSalePostResponse> getPostsPromotional(int page, int size) {
        // Danh sách các status cần loại bỏ
        List<String> excludedStatuses = Arrays.asList("EXPIRED", "PENDING", "REJECTED");

        Pageable pageable = PageRequest.of(page - 1, size);

        // Lấy các phòng khuyến mãi, loại bỏ các phòng có status nằm trong danh sách excludedStatuses
        Page<PromotionalRoomEntity> promotionalRoomEntities = promotionalRepository.findAll(pageable);

        // Lọc các phòng khuyến mãi theo status và sắp xếp các bài đăng theo index (tăng dần) và createDate (tăng dần)
        List<RoomSalePostResponse> postResponses = promotionalRoomEntities.getContent().stream()
                .filter(entity -> entity.getFixPrice() != null) // Giữ lại những bài đăng có giá fixPrice không null
                .flatMap(promotionalRoomEntity -> {
                    // Lấy danh sách các bài đăng của phòng khuyến mãi theo roomId
                    List<RoomSalePostEntity> roomSalePosts = roomSalePostRepository.findAllByRoomId(promotionalRoomEntity.getRoomId());

                    // Sắp xếp các bài đăng theo index (tăng dần) và createDate (tăng dần)
                    List<RoomSalePostEntity> sortedRoomSalePosts = roomSalePosts.stream()
                            .sorted(Comparator.comparing(RoomSalePostEntity::getIndex) // Sắp xếp theo index tăng dần
                                    .thenComparing(RoomSalePostEntity::getCreatedDate)) // Sắp xếp theo createDate tăng dần
                            .collect(Collectors.toList());

                    // Chuyển đổi các bài đăng thành RoomSalePostResponse
                    return sortedRoomSalePosts.stream()
                            .filter(roomSalePostEntity -> !excludedStatuses.contains(roomSalePostEntity.getStatus())) // Lọc theo status
                            .map(roomSalePostEntity -> marketingMapper.toRoomSalePostPromotionalResponse(promotionalRoomEntity, roomSalePostEntity));
                })
                .toList();

        // Trả về PageResponse với thông tin của các bài đăng đã sắp xếp
        return PageResponse.<RoomSalePostResponse>builder()
                .currentPage(page)
                .data(postResponses)
                .pageSize(promotionalRoomEntities.getSize())
                .totalElements(postResponses.size())
                .totalPages((int) Math.ceil((double) postResponses.size() / size))
                .build();
    }


    @Override
    public RoomSalePostResponse getPostByPromotional(String id) {
        var roomSalePostEntity = roomSalePostRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        var promotionalRoomEntity = promotionalRepository.findByRoomId(roomSalePostEntity.getRoomId())
                .orElseThrow(() -> new AppException(ErrorCode.PROMOTIONAL_NOT_FOUND));
        var roomSalePostResponse = marketingMapper.toRoomSalePostPromotionalResponse(promotionalRoomEntity, roomSalePostEntity);
        roomSalePostResponse.setFixPrice(promotionalRoomEntity.getFixPrice());
        return roomSalePostResponse;
    }


    @Override
    public PageResponse<RoomSalePostResponse> searchTerm(String searchRequest, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<RoomSalePostEntity> pageData = roomSalePostRepository.searchByText(searchRequest, pageable);

        List<PromotionalRoomEntity> promotionalEntities = promotionalRepository.findAll();

        Map<String, PromotionalRoomEntity> promotionalRoomMap = promotionalEntities.stream()
                .collect(Collectors.toMap(PromotionalRoomEntity::getRoomId, Function.identity()));

        List<RoomSalePostResponse> roomSalePostList = pageData.getContent().stream().map(roomSalePostEntity -> {
            RoomSalePostResponse roomSalePostResponse;
            PromotionalRoomEntity promotionalRoomEntity = promotionalRoomMap.get(roomSalePostEntity.getRoomId());

            if (promotionalRoomEntity != null) {
                roomSalePostResponse = marketingMapper.toRoomSalePostPromotionalResponse(promotionalRoomEntity, roomSalePostEntity);
                roomSalePostResponse.setFixPrice(promotionalRoomEntity.getFixPrice());
            } else {
                roomSalePostResponse = marketingMapper.toResponseRoomSalePost(roomSalePostEntity);
            }

            roomSalePostResponse.setCreated(dateTimeFormatter.format(roomSalePostEntity.getCreatedDate()));
            return roomSalePostResponse;
        }).toList();

        return PageResponse.<RoomSalePostResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(roomSalePostList)
                .build();
    }

    @Override
    public PageResponse<RoomSalePostResponse> searchPosts(SearchPostRequest searchRequest, int page, int size) {
        Sort sort = Sort.by("createDate").descending();
        PageRequest pageable = PageRequest.of(page - 1, size, sort);
        Query query = mongoDBQuery.buildSearchQuery(searchRequest);
        long totalElements = mongoTemplate.count(query, RoomSalePostEntity.class);
        query.with(pageable);
        List<RoomSalePostEntity> roomEntities = mongoTemplate.find(query, RoomSalePostEntity.class);
        List<RoomSalePostResponse> postResponses = roomEntities.stream()
                .map(marketingMapper::toResponseRoomSalePost)
                .toList();
        return PageResponse.<RoomSalePostResponse>builder()
                .currentPage(page)
                .data(postResponses)
                .pageSize(size)
                .totalElements(totalElements)
                .totalPages((int) Math.ceil((double) totalElements / size))
                .build();
    }

    @Override
    public PageResponse<RoomSalePostResponse> getPostFilter(FilterRequest filterRequest, int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);

        // Tạo aggregation để lấy dữ liệu, đã lọc theo trạng thái
        Aggregation aggregation = mongoDBQuery.buildFilterAggregation(filterRequest, pageable);

        // Lấy kết quả thực tế (các phần tử đã lọc theo trạng thái)
        List<RoomSalePostEntity> roomEntities = mongoTemplate.aggregate(aggregation, "roomSalePosts", RoomSalePostEntity.class).getMappedResults();

        // Đếm tổng số phần tử sau khi đã lọc trạng thái
        long totalElements = getTotalFilteredCount(filterRequest);

        // Chuyển đổi và trả về kết quả
        List<RoomSalePostResponse> postResponses = convertRoomEntitiesToResponse(roomEntities);
        return createPageResponse(page, size, (int) totalElements, postResponses);
    }

    private List<RoomSalePostResponse> convertRoomEntitiesToResponse(List<RoomSalePostEntity> roomEntities) {
        // Danh sách status cần loại bỏ
        List<String> excludedStatuses = Arrays.asList("EXPIRED", "PENDING", "REJECTED");

        // Lấy danh sách các phòng khuyến mãi và ánh xạ thành Map để dễ tra cứu
        List<PromotionalRoomEntity> promotionalEntities = promotionalRepository.findAll();
        Map<String, PromotionalRoomEntity> promotionalRoomMap = promotionalEntities.stream()
                .collect(Collectors.toMap(PromotionalRoomEntity::getRoomId, Function.identity()));

        // Lọc và chuyển đổi danh sách RoomSalePostEntity thành RoomSalePostResponse
        return roomEntities.stream()
                // Bỏ qua các bài đăng có trạng thái nằm trong excludedStatuses
                .filter(roomSalePostEntity -> !excludedStatuses.contains(roomSalePostEntity.getStatus()))
                .map(roomSalePostEntity -> {
                    RoomSalePostResponse roomSalePostResponse;
                    // Kiểm tra xem phòng có thuộc danh sách khuyến mãi không
                    PromotionalRoomEntity promotionalRoomEntity = promotionalRoomMap.get(roomSalePostEntity.getRoomId());

                    if (promotionalRoomEntity != null) {
                        // Nếu có khuyến mãi, sử dụng mapper tương ứng
                        roomSalePostResponse = marketingMapper.toRoomSalePostPromotionalResponse(promotionalRoomEntity, roomSalePostEntity);
                        roomSalePostResponse.setFixPrice(promotionalRoomEntity.getFixPrice());
                    } else {
                        // Nếu không có khuyến mãi, sử dụng mapper mặc định
                        roomSalePostResponse = marketingMapper.toResponseRoomSalePost(roomSalePostEntity);
                    }

                    // Định dạng ngày tạo và thêm vào response
                    roomSalePostResponse.setCreated(dateTimeFormatter.format(roomSalePostEntity.getCreatedDate()));
                    return roomSalePostResponse;
                })
                .toList();
    }

    private long getTotalFilteredCount(FilterRequest filterRequest) {
        // Tạo aggregation để đếm số lượng các bài đăng không có trạng thái bị loại trừ
        List<AggregationOperation> operations = new ArrayList<>();

        // Thêm các bộ lọc khác nếu có
        operations.addAll(mongoDBQuery.buildCountOperations(filterRequest));

        // Thêm điều kiện lọc status
        operations.add(Aggregation.match(Criteria.where("status").nin("EXPIRED", "PENDING", "REJECTED")));

        // Tạo aggregation từ danh sách các operations
        Aggregation aggregation = Aggregation.newAggregation(operations);

        // Đếm tổng số bài đăng phù hợp
        return mongoTemplate.aggregate(aggregation, "roomSalePosts", RoomSalePostEntity.class).getMappedResults().size();
    }

}
