package com.roomfinder.marketing.repositories.entities;

import jakarta.annotation.PostConstruct;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "banners")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BannerEntity extends BaseEntity{
    @Id
    String id; // Unique identifier for the post
    @Field("description")
    String description; // Detailed description of the banner
    @PostConstruct
    public void initializeCreationDetails() {
        Instant now = Instant.now();
        if (this.getCreatedDate() == null) {
            this.setCreatedDate(now);
        }
        if (this.getCreatedBy() == null) {
            this.setCreatedBy("System"); // Default or placeholder value
        }
    }
}
