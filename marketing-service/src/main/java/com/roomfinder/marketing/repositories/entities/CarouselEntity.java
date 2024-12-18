package com.roomfinder.marketing.repositories.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "carouselBanner")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CarouselEntity {
    @Id
    String id; // Unique identifier for the post

    @Field("name")
    String name;

    @Field("images")

    Set<PostImage> postImages = new HashSet<>();
}
