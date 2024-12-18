package com.roomfinder.marketing.mappers;

import com.roomfinder.marketing.controllers.dto.request.PromotionalRequest;
import com.roomfinder.marketing.controllers.dto.response.PromotionalResponse;
import com.roomfinder.marketing.repositories.entities.PromotionalRoomEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PromotionalMapper {
    PromotionalRoomEntity toCreatePromotionalRoom(PromotionalRequest request);

    PromotionalResponse toPromotionalRoomResponse (PromotionalRoomEntity entity);
    @Mapping(target = "id", ignore = true)
    void updatePromotionalRoom(PromotionalRequest request, @MappingTarget PromotionalRoomEntity entity);
}
