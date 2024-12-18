package com.roomfinder.marketing.services;

import com.roomfinder.marketing.controllers.dto.PageResponse;
import com.roomfinder.marketing.controllers.dto.request.BaseIndexRequest;
import com.roomfinder.marketing.controllers.dto.response.BaseIndexResponse;
import com.roomfinder.marketing.controllers.dto.response.RoomSalePostResponse;
import com.roomfinder.marketing.repositories.entities.FeaturedRoomEntity;


public interface BaseIndexService {
    RoomSalePostResponse createFeaturedAdsFee(int typePackage,String roomId);

    BaseIndexResponse updateFeatured(String id, BaseIndexRequest featuredRequest);

    void deleteFeatured(String id);

    BaseIndexResponse getFeaturedById(String id);

    PageResponse<BaseIndexResponse> getFeatures(int page, int size);
}
