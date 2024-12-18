package com.roomfinder.marketing.services;

import com.roomfinder.marketing.controllers.dto.PageResponse;
import com.roomfinder.marketing.controllers.dto.request.FavoriteRequest;
import com.roomfinder.marketing.controllers.dto.response.BaseIndexResponse;
import com.roomfinder.marketing.controllers.dto.response.FavoriteResponse;


public interface FavoriteService {
    FavoriteResponse createFavorite(FavoriteRequest request );
    void deleteFavorite(String id);
    PageResponse<FavoriteResponse> getFavorite(int page, int size);
}
