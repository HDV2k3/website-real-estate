package com.roomfinder.marketing.mappers;

import com.roomfinder.marketing.controllers.dto.request.FavoriteRequest;
import com.roomfinder.marketing.controllers.dto.response.FavoriteResponse;
import com.roomfinder.marketing.repositories.entities.FavoriteEntity;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface FavoriteMapper {
    FavoriteEntity toCreateFavoriteRoom(FavoriteRequest request);
    FavoriteResponse toFavoriteResponse (FavoriteEntity entity);
}
