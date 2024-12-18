package com.roomfinder.marketing.services.impl;

import com.roomfinder.marketing.controllers.dto.request.CarouselRequest;
import com.roomfinder.marketing.controllers.dto.response.CarouselResponse;
import com.roomfinder.marketing.exception.AppException;
import com.roomfinder.marketing.exception.ErrorCode;
import com.roomfinder.marketing.mappers.CarouselMapper;
import com.roomfinder.marketing.repositories.CarouselRepository;
import com.roomfinder.marketing.services.CarouselService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CarouselServiceImpl implements CarouselService
{
    CarouselMapper carouselMapper;
    CarouselRepository carouselRepository;
    private static final Random RANDOM = new Random();

    public static String generateCarouselId() {
        int randomNum = RANDOM.nextInt(9) + 1;
        return String.format("carousel%01d", randomNum);
    }
    @Override
    public CarouselResponse createdCarousel(CarouselRequest request) {
        var carousel = carouselMapper.toCreatedCarousel(request);
        carousel.setId(generateCarouselId());
        var carouselEntity = carouselRepository.save(carousel);
        return carouselMapper.toResponseCarousel(carouselEntity);
    }

    @Override
    public List<CarouselResponse> getCarousels() {
        var carousels = carouselRepository.findAll();
        return carouselMapper.toResponseListCarousel(carousels);
    }

    @Override
    public void deleteCarousel(String id) {
        carouselRepository.findById(id)
                .ifPresentOrElse(carouselRepository::delete,
                        () -> {
                            throw new AppException(ErrorCode.CAROUSEL_NOT_FOUND);
                        });
    }
}
