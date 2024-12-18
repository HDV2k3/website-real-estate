package com.roomfinder.marketing.facade;

import com.roomfinder.marketing.controllers.dto.request.BannerRequest;
import com.roomfinder.marketing.controllers.dto.response.BannerResponse;
import com.roomfinder.marketing.services.BannerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BannerFacade {
    BannerService bannerService;

    //createBanner
    public BannerResponse createBanner(BannerRequest bannerRequest){
        return bannerService.createBanner(bannerRequest);
    }
    //updateBanner
    public BannerResponse updateBanner(BannerRequest bannerRequest, String id){
        return bannerService.updateBanner(bannerRequest, id);
    }
    //deleteBanner
    public String deleteBanner(String id){
        return bannerService.deleteBanner(id);
    }
    //getBanners
    public List<BannerResponse> getBanners(){
        return bannerService.getBanners();
    }

}
