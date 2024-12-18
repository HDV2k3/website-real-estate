package com.roomfinder.marketing.controllers.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InfoMarketing {
    int quantityTypeSaleRent;
    int quantityTypeSale;
    int quantityUser;
    int quantityBroker;
    int totalPosts;
}
