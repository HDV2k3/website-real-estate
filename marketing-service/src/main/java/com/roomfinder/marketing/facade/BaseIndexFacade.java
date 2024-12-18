package com.roomfinder.marketing.facade;

import com.roomfinder.marketing.controllers.dto.PageResponse;
import com.roomfinder.marketing.controllers.dto.request.BaseIndexRequest;
import com.roomfinder.marketing.controllers.dto.response.BaseIndexResponse;
import com.roomfinder.marketing.controllers.dto.response.RoomSalePostResponse;
import com.roomfinder.marketing.repositories.entities.FeaturedRoomEntity;
import com.roomfinder.marketing.services.BaseIndexService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BaseIndexFacade {
    BaseIndexService featuredService;

    public RoomSalePostResponse createFeaturedAdsFee(int typePackage,String roomId) {
        return featuredService.createFeaturedAdsFee(typePackage,roomId);
    }


    public BaseIndexResponse updateFeatured(String id, BaseIndexRequest featuredRequest) {
        return featuredService.updateFeatured(id, featuredRequest);
    }

    public String deleteFeatured(String id) {
        featuredService.deleteFeatured(id);
        return "Delete Featured Successfully";
    }

    public BaseIndexResponse getFeaturedById(String id) {
        return featuredService.getFeaturedById(id);
    }

    public PageResponse<BaseIndexResponse> getFeatures(int page, int size) {
        return featuredService.getFeatures(page, size);
    }




}
