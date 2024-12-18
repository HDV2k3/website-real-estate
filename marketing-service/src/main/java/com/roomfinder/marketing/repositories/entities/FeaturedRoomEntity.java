package com.roomfinder.marketing.repositories.entities;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "featuredRoom")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeaturedRoomEntity extends BaseEntity{
    @Id
    String id; // Unique identifier for the post

    @Field("roomId")
    String roomId; // Identifier of the room being advertised

    @Field("index")
    @Min(value = Integer.MIN_VALUE, message = "Index must be negative") // Ensures the index is negative
    @Indexed(unique = true) // Enforces uniqueness
    Integer index;
    @Field("expiry")
    private Instant expiry;
    @Field("type")
//    private Integer type;
    LinkedList<Integer> types = new LinkedList<>();
    @PostConstruct
    public void initializeCreationDetails() {
        Instant now = Instant.now();
        if (this.getCreatedDate() == null) {
            this.setCreatedDate(now);
        }
    }
}
