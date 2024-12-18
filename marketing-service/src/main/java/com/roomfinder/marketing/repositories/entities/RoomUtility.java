package com.roomfinder.marketing.repositories.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomUtility {
    @Field("furnitureAvailability")
    Map<String, Boolean> furnitureAvailability ; // Map of furniture items and their availability (true/false)

    @Field("amenitiesAvailability")
    Map<String, Boolean> amenitiesAvailability ; // Map of additional amenities and their availability (true/false)
}
