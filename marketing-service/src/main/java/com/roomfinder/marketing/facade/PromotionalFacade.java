package com.roomfinder.marketing.facade;

import com.roomfinder.marketing.controllers.dto.PageResponse;
import com.roomfinder.marketing.controllers.dto.request.PromotionalRequest;
import com.roomfinder.marketing.controllers.dto.response.PromotionalResponse;
import com.roomfinder.marketing.services.PromotionalService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PromotionalFacade {
    PromotionalService promotionalService;

    public PromotionalResponse createPromotional(PromotionalRequest promotionalRequest) {
        return promotionalService.createPromotional(promotionalRequest);
    }

    public PromotionalResponse updatePromotional(String id, PromotionalRequest promotionalRequest) {
        return promotionalService.updatePromotional(id, promotionalRequest);
    }

    public String deletePromotional(String id) {
        promotionalService.deletePromotional(id);
        return "Delete Promotional Successfully";
    }

    public PromotionalResponse getPromotionalById(String id) {
        return promotionalService.getPromotionalById(id);
    }

    public PageResponse<PromotionalResponse> getListPromotional(int page, int size) {
        return promotionalService.getListPromotional(page, size);
    }
}
