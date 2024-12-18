package com.roomfinder.marketing.controllers.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchPostRequest {
    String title;
    String description;
    String type;
    String name;
    String address;
    String basePrice;
    String totalArea;
}
