package com.roomfinder.marketing.repositories.entities;

import com.roomfinder.marketing.controllers.dto.response.PostImageResponse;
import jakarta.annotation.PostConstruct;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "hospitalRoom")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HospitalRoomEntity extends BaseEntity {
    @Id
    String id;

    @Field(name = "name")
    String name;
    @Field(name = "promotional")

    String promotional;

    @Field("images")
    Set<PostImage> postImages = new HashSet<>(); // URLs of images of the room
    @Field(name = "price")

    Double price;
    @Field(name = "description")

    String description;
    @Field(name = "utility")

    List<String> utility;
    @Field(name = "contact")

    String contact;
    @PostConstruct
    public void initializeCreationDetails() {
        Instant now = Instant.now();
        if (this.getCreatedDate() == null) {
            this.setCreatedDate(now);
        }
    }
}
