package com.roomfinder.marketing.facade;

import com.roomfinder.marketing.controllers.dto.PageResponse;
import com.roomfinder.marketing.controllers.dto.request.NewsRequest;
import com.roomfinder.marketing.controllers.dto.response.NewsResponse;
import com.roomfinder.marketing.services.RealEstateExperienceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RealEstateExperienceFacade {
    // The RealEstateExperienceFacade class provides methods to create, read, update, and delete.
    // createRealEstateExperience
    // getAllRealEstateExperience
    // getRealEstateExperienceById
    // updateRealEstateExperienceById
    // deleteRealEstateExperienceById
    RealEstateExperienceService realEstateExperienceService;
    public NewsResponse createRealEstateExperience(NewsRequest request) {
        return realEstateExperienceService.createNews(request);
    }

    public PageResponse<NewsResponse> getAllRealEstateExperience(int page, int size) {
        return realEstateExperienceService.getAllNews(page, size);
    }

    public NewsResponse getRealEstateExperienceById(String id) {
        return realEstateExperienceService.getNewsById(id);
    }

    public NewsResponse updateRealEstateExperienceById(NewsRequest request, String id) {
        return realEstateExperienceService.updateNewsById(id, request);
    }

    public String deleteRealEstateExperienceById(String id) {
        return realEstateExperienceService.deleteNewsById(id);
    }


}
