package com.roomfinder.marketing.repositories.entities;

import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "realEstateExperience")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RealEstateExperienceEntity {
    @Id
    String id;
    @Field("title")
    String title;
    @Field("description")
    String description;
    @Field("images")
    Set<PostImage> postImages = new HashSet<>();
}
