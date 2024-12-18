package com.roomfinder.marketing.facade;

import com.roomfinder.marketing.controllers.dto.PageResponse;
import com.roomfinder.marketing.controllers.dto.response.RoomSalePostResponse;
import com.roomfinder.marketing.services.MarketingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchFacade {
    private final MarketingService marketingService;

    public PageResponse<RoomSalePostResponse> searchPosts(String searchTerm, int page, int size) {
        return marketingService.searchTerm(searchTerm, page, size);
    }
}