package com.roomfinder.marketing.utility;

import com.roomfinder.marketing.controllers.dto.request.RoomSalePostRequest;
import org.springframework.util.StringUtils;

public class RoomSalePostRequestValidator {

    public static boolean validate(RoomSalePostRequest request) {
        if (request == null) {
            return true;
        }
        if (!StringUtils.hasText(request.getTitle())) {
            return true;
        }
        if (request.getRoomInfo() == null) {
            return true;
        }
        if (request.getPricingDetails().getBasePrice() == null) {
            return true;
        }
        if (request.getPricingDetails().getElectricityCost() == null) {
            return true;
        }
        if (request.getPricingDetails().getWaterCost() == null) {
            return true;
        }
        if (!StringUtils.hasText(request.getContactInfo())) {
            return true;
        }
        if (!StringUtils.hasText(request.getStatusShow())) {
            return true;
        }

        return false;
    }

}
