package com.roomfinder.marketing.controllers.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.roomfinder.marketing.controllers.dto.response.PostImageResponse;
import com.roomfinder.marketing.utility.CustomInstantDeserializer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public  class RoomInfoRequest {
    String name;
    String description;
    String address;
    int district;
    int commune;
    int type;
    int style;
    int typeSale;
    int floor;
    Double width;
    Double height;
    Double totalArea;
    Integer capacity;
    Integer numberOfBedrooms;
    Integer numberOfBathrooms;
    @JsonDeserialize(using = CustomInstantDeserializer.class)
    Instant availableFromDate;
}