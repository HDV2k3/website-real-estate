package com.roomfinder.marketing.mappers;

import com.roomfinder.marketing.controllers.dto.request.RoomSalePostRequest;
import com.roomfinder.marketing.controllers.dto.response.RoomSalePostResponse;
import com.roomfinder.marketing.repositories.entities.PromotionalRoomEntity;
import com.roomfinder.marketing.repositories.entities.RoomSalePostEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")

public interface MarketingMapper {


    RoomSalePostEntity toCreateRoomSalePost(RoomSalePostRequest request);

    RoomSalePostResponse toResponseRoomSalePost(RoomSalePostEntity entity);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roomId",ignore = true)
    void updateRoomSalePost(RoomSalePostRequest request, @MappingTarget RoomSalePostEntity entity);
    @Mapping(source = "roomSalePostEntity.id", target = "id")
    @Mapping(source = "promotionalRoomEntity.roomId", target = "roomId")
    RoomSalePostResponse toRoomSalePostPromotionalResponse(
            PromotionalRoomEntity promotionalRoomEntity,
            RoomSalePostEntity roomSalePostEntity
    );
}
