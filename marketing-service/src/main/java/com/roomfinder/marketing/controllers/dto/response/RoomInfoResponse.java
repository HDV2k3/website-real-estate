package com.roomfinder.marketing.controllers.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public  class RoomInfoResponse {
    String name;
    String description;
    String address;
    int district;
    int commune;
    int type;
    int style;
    int typeSale;
    int floor;
    List<PostImageResponse> postImages;
    Double width;
    Double height;
    Double totalArea;
    Integer capacity;
    Integer numberOfBedrooms;
    Integer numberOfBathrooms;
    Instant availableFromDate;
}