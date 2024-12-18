package com.roomfinder.marketing.controllers.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CarouselRequest {
    String name;
    List<String> images;
}