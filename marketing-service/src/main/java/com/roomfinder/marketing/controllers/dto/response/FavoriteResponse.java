package com.roomfinder.marketing.controllers.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FavoriteResponse {
    String id;
    RoomSalePostResponse roomSalePostResponse;
    int userId;
    Integer index;
}
