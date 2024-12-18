package com.roomfinder.marketing.controllers.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilterRequest {
    BigDecimal minPrice;
    BigDecimal maxPrice;
    int district;
    int commune;
    int type;
    Boolean hasPromotion;
    String sortByPrice;
    String sortByCreated;
}
