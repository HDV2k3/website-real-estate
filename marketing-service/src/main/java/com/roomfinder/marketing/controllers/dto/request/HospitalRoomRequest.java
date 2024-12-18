package com.roomfinder.marketing.controllers.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HospitalRoomRequest {
    String name;
    String promotional;
    Double price;
    String description;
    List<String> utility;
    String contact;
}
