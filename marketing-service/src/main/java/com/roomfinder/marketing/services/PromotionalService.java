package com.roomfinder.marketing.services;

import com.roomfinder.marketing.controllers.dto.PageResponse;
import com.roomfinder.marketing.controllers.dto.request.PromotionalRequest;
import com.roomfinder.marketing.controllers.dto.response.PromotionalResponse;

public interface PromotionalService {
    PromotionalResponse createPromotional(PromotionalRequest promotionalRequest);

    PromotionalResponse updatePromotional(String id, PromotionalRequest promotionalRequest);

    void deletePromotional(String id);

    PromotionalResponse getPromotionalById(String id);

    PageResponse<PromotionalResponse> getListPromotional(int page, int size);
}
