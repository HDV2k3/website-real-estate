//package com.roomfinder.marketing.repositories.entities;
//
//
//import jakarta.annotation.PostConstruct;
//import lombok.*;
//import lombok.experimental.FieldDefaults;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;
//import org.springframework.data.mongodb.core.mapping.Field;
//
//import java.time.Instant;
//import java.time.LocalDate;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@Document(collection = "roomSalePosts")
//@FieldDefaults(level = AccessLevel.PRIVATE)
//public class RoomSalePostEntity extends BaseEntity {
//
//    @Id
//    String id; // Unique identifier for the post
//
//    @Field("roomId")
//    String roomId; // Identifier of the room being advertised
//
//    @Field("title")
//    String title; // Title of the post
//
//    @Field("description")
//    String description; // Detailed description of the room
//
//    @Field("roomInfo")
//    RoomInfo roomInfo; // Main information about the room, such as name and address
//
//    @Field("roomUtility")
//    RoomUtility roomUtility; // Details about utilities and furniture available in the room
//
//    @Field("pricingDetails")
//    PricingDetail pricingDetails; // Detailed information on pricing, services, and fees associated with the room
//
//    @Field("availableFromDate")
//    Instant availableFromDate; // Date when the room becomes available
//
//    @Field("contactInfo")
//    String contactInfo; // Contact information for inquiries
//
//    @Field("additionalDetails")
//    String additionalDetails; // Any additional details or special features of the
//
//    @Field("status")
//    String status; // Status of the post (e.g., active, closed)
//
//    @Field("statusShow")
//    String statusShow;
//
//    @Field("created")
//    String created;
//
//    @Field("index")
//    int index;
//
//    @PostConstruct
//    public void initializeCreationDetails() {
//        Instant now = Instant.now();
//        if (this.getCreatedDate() == null) {
//            this.setCreatedDate(now);
//        }
//    }
//}
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
@Document(collection = "roomSalePosts")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomSalePostEntity extends BaseEntity {

    @Id
    String id;

    @Field("roomId")
    String roomId;

    @Field("title")
    String title;

    @Field("description")
    String description;

    @Field("roomInfo")
    RoomInfo roomInfo;

    @Field("roomUtility")
    RoomUtility roomUtility;

    @Field("pricingDetails")
    PricingDetail pricingDetails;

    @Field("availableFromDate")
    Instant availableFromDate;

    @Field("contactInfo")
    String contactInfo;

    @Field("additionalDetails")
    String additionalDetails;

    @Field("status")
    String status;

    @Field("statusShow")
    String statusShow;

    @Field("created")
    String created;

    @Field("index")
    int index;
    @Field("typePackage")
    int typePackage;
    @PostConstruct
    public void initializeCreationDetails() {
        Instant now = Instant.now();
        if (this.getCreatedDate() == null) {
            this.setCreatedDate(now);
        }
    }
}