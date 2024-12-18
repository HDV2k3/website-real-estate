package com.roomfinder.marketing.services;

import com.roomfinder.marketing.controllers.dto.request.CarouselRequest;
import com.roomfinder.marketing.controllers.dto.response.CarouselResponse;

import java.util.List;

public interface CarouselService {
    CarouselResponse createdCarousel(CarouselRequest request);

    List<CarouselResponse> getCarousels();

    void  deleteCarousel(String id);

}