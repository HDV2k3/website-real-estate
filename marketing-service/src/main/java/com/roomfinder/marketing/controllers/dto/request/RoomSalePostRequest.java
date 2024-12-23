package com.roomfinder.marketing.controllers.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.roomfinder.marketing.utility.CustomInstantDeserializer;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomSalePostRequest {
    String title;
    String description;
    RoomInfoRequest roomInfo;
    RoomUtilityRequest roomUtility;
    PricingDetailsRequest pricingDetails;
    @JsonDeserialize(using = CustomInstantDeserializer.class)
    Instant availableFromDate;
    String contactInfo;
    String additionalDetails;
    String statusShow;
    int typePackage;
}
