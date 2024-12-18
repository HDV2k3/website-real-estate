package com.roomfinder.marketing.services;

import com.roomfinder.marketing.controllers.dto.PageResponse;
import com.roomfinder.marketing.controllers.dto.request.NewsRequest;
import com.roomfinder.marketing.controllers.dto.response.NewsResponse;

public interface RealEstateExperienceService {
    // create a new market and trend
    NewsResponse createNews(NewsRequest newsService);
    // get all market and trend
    PageResponse<NewsResponse> getAllNews(int page, int size);
    // get market and trend by id
    NewsResponse getNewsById(String id);
    // update market and trend by id
    NewsResponse updateNewsById(String id, NewsRequest newsService);
    // delete market and trend by id
    String deleteNewsById(String id);

}
