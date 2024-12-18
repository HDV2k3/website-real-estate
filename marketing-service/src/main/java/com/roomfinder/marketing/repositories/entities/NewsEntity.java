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
@Document(collection = "news")
@FieldDefaults(level = AccessLevel.PRIVATE)

public class NewsEntity {
    @Id
    String id;
    @Field("title")
    String title;
    @Field("description")
    String description;
    @Field("images")
    Set<PostImage> postImages = new HashSet<>();
}
