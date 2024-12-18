package com.roomfinder.marketing.services.impl;


import com.roomfinder.marketing.controllers.dto.request.BannerRequest;
import com.roomfinder.marketing.controllers.dto.response.BannerResponse;
import com.roomfinder.marketing.exception.AppException;
import com.roomfinder.marketing.exception.ErrorCode;
import com.roomfinder.marketing.repositories.BannerRepository;
import com.roomfinder.marketing.repositories.entities.BannerEntity;
import com.roomfinder.marketing.services.BannerService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BannerServiceImpl implements BannerService {
    BannerRepository bannerRepository;
    @Override
    public BannerResponse createBanner(BannerRequest bannerRequest) {
        BannerEntity bannerEntity = BannerEntity.builder()
                .description(bannerRequest.getDescription())
                .build();
        bannerRepository.save(bannerEntity);
        return BannerResponse.builder()
                .id(bannerEntity.getId())
                .description(bannerEntity.getDescription())
                .build();
    }

    @Override
    public BannerResponse updateBanner(BannerRequest bannerRequest, String id) {
     return bannerRepository.findById(id)
             .map(bannerEntity -> {
                 bannerEntity.setDescription(bannerRequest.getDescription());
                 bannerRepository.save(bannerEntity);
                 return BannerResponse.builder()
                         .id(bannerEntity.getId())
                         .description(bannerEntity.getDescription())
                         .build();
             })
             .orElseThrow(() -> new AppException(ErrorCode.BANNER_NOT_FOUND));
    }

    @Override
    public String deleteBanner(String id) {
        bannerRepository.deleteById(id);
        return "Delete Successful";
    }

    @Override
    public List<BannerResponse> getBanners() {
        return bannerRepository.findAll().stream()
                .map(bannerEntity -> BannerResponse.builder()
                        .id(bannerEntity.getId())
                        .description(bannerEntity.getDescription())
                        .build())
                .toList();
    }
}
