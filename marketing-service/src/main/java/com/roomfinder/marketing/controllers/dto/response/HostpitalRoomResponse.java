package com.roomfinder.marketing.controllers.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HostpitalRoomResponse {
    String id;
    String name;
    String promotional;
    List<PostImageResponse> postImages;
    Double price;
    String description;
    List<String> utility;
    String contact;
}
