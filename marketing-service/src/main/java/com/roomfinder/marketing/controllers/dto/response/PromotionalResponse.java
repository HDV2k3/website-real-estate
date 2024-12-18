package com.roomfinder.marketing.controllers.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromotionalResponse {
    String id;
    String roomId;
    BigDecimal fixPrice;
    Double percent;
}
