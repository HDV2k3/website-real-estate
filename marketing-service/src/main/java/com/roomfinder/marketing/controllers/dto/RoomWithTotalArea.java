package com.roomfinder.marketing.controllers.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomWithTotalArea {
    String name;
    double totalArea;
    double width;
    double height;
}
