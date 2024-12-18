package com.roomfinder.marketing.mappers;

import com.roomfinder.marketing.controllers.dto.request.CarouselRequest;
import com.roomfinder.marketing.controllers.dto.response.CarouselResponse;
import com.roomfinder.marketing.repositories.entities.CarouselEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CarouselMapper {
    CarouselEntity toCreatedCarousel(CarouselRequest request);

    CarouselResponse toResponseCarousel(CarouselEntity entity);

    List<CarouselResponse> toResponseListCarousel(List<CarouselEntity> entities);

}