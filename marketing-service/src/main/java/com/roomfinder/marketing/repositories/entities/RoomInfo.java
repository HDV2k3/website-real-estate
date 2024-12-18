package com.roomfinder.marketing.repositories.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomInfo {

    // Identification and Description
    @Field("name")
    String name; // Name or identifier of the room

    @Field("address")
    String address; // Full address of the room

    @Field("district")
    int district;

    @Field("commune")
    int commune;

    @Field("typeSale")
    int typeSale;

    @Field("description")
    String description; // Description of the room
    // Type and Design
    @Field("type")
    int type; // Type of the room (e.g., studio, apartment)

    @Field("style")
    int style; // Style or design of the room (e.g., modern, traditional)

    // Location Details
    @Field("floor")
    int floor; // Floor on which the room is located

    @Field("images")
    Set<PostImage> postImages = new HashSet<>(); // URLs of images of the room
    // Dimensions
    @Field("width")
    double width; // Width of the room in meters or feet

    @Field("height")
    double height; // Height of the room in meters or feet

    @Field("totalArea")
    double totalArea; // Total area of the room in square meters or feet

    // Capacity and Features
    @Field("capacity")
    int capacity; // Capacity of the room

    @Field("numberOfBedrooms")
    int numberOfBedrooms; // Number of bedrooms in the room

    @Field("numberOfBathrooms")
    int numberOfBathrooms; // Number of bathrooms in the room

    @Field("availableFromDate")
    Instant availableFromDate; // room will be available for rent.
}