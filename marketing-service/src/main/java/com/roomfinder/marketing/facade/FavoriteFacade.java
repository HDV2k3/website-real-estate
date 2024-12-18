package com.roomfinder.marketing.facade;

import com.roomfinder.marketing.controllers.dto.PageResponse;
import com.roomfinder.marketing.controllers.dto.request.FavoriteRequest;
import com.roomfinder.marketing.controllers.dto.response.FavoriteResponse;
import com.roomfinder.marketing.services.FavoriteService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FavoriteFacade {

    FavoriteService favoriteService;

    public FavoriteResponse create(FavoriteRequest request)
    {
        return favoriteService.createFavorite(request);
    }

    public String delete(String roomId)
    {
        favoriteService.deleteFavorite(roomId);
        return "Delete Success";
    }

    public PageResponse<FavoriteResponse> getFavorites(int page, int size)
    {
        return favoriteService.getFavorite(page,size);
    }
}
