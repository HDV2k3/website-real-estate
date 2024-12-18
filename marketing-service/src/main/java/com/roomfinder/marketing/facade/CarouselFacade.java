package com.roomfinder.marketing.facade;

import com.roomfinder.marketing.controllers.dto.request.CarouselRequest;
import com.roomfinder.marketing.controllers.dto.response.CarouselResponse;
import com.roomfinder.marketing.services.CarouselService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CarouselFacade {
    CarouselService carouselService;
    public CarouselResponse created(CarouselRequest request)
    {
        return carouselService.createdCarousel(request);
    }

    public List<CarouselResponse> getCarousels()
    {
        return carouselService.getCarousels();
    }

    public String deleteCarousel(String id)
    {
        carouselService.deleteCarousel(id);
        return "Delete Successfully";
    }
}