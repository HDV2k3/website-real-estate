package com.roomfinder.marketing.repositories.datamodel;
import com.roomfinder.marketing.controllers.dto.request.PricingDetailsRequest;
import com.roomfinder.marketing.controllers.dto.request.RoomInfoRequest;
import com.roomfinder.marketing.controllers.dto.request.RoomUtilityRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Room {
    String roomId;
    RoomInfoRequest roomInfo;
    RoomUtilityRequest roomUtility;
    PricingDetailsRequest pricingDetails;
}