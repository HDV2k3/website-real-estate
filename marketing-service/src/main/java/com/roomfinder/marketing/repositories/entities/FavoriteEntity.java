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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "favoriteRoom")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FavoriteEntity extends BaseEntity{

    @Id
    String id;
    @Field("roomId")
    String roomId;
    @Field("index")
    @Min(value = Integer.MIN_VALUE, message = "Index must be negative") // Ensures the index is negative
    @Indexed(unique = true) // Enforces uniqueness
    Integer index;
    @PostConstruct
    public void initializeCreationDetails() {
        Instant now = Instant.now();
        if (this.getCreatedDate() == null) {
            this.setCreatedDate(now);
        }
    }
}
