package com.roomfinder.marketing.repositories.entities;

import com.roomfinder.marketing.constants.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "incentivePrograms")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IncentiveProgramEntity {
    @Id
    String id; // Unique identifier for the incentive program

    @Field("name")
    String name; // Name of the incentive program
    @Field("description")
    String description; // Description of the incentive program
    @Field("type")
    String type; // Type of the incentive program

    @Field("start_date")
    Instant startDate; // Start date of the incentive program
    @Field("end_date")
    Instant endDate; // End date of the incentive program
    @Field("status")
    Status status; // Status of the incentive program
    @Field("images")

    Set<PostImage> postImages = new HashSet<>();

}
