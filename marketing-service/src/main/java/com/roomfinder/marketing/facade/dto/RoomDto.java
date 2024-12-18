package com.roomfinder.marketing.facade.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomDto {
    String id;
    String name;
}
