package com.roomfinder.marketing.controllers.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public  class PricingDetailsResponse {
    BigDecimal basePrice;
    BigDecimal electricityCost;
    BigDecimal waterCost;
    List<FeeDetailResponse> additionalFees;
}