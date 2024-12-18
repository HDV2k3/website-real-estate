package com.roomfinder.marketing.repositories.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "promotionalRoom")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromotionalRoomEntity  {
    @Id
    String id; // Unique identifier for the post

    @Field("roomId")
    String roomId; // Identifier of the room being advertised

    @Field("fixPrice")
    BigDecimal fixPrice; // discount by fix a price. Ex: 500k VND

    @Field("percent")
    Double percent; // discount by a percent. Ex: 10%
}
