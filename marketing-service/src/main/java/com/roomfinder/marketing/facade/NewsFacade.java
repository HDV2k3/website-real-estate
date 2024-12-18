package com.roomfinder.marketing.facade;

import com.roomfinder.marketing.controllers.dto.PageResponse;
import com.roomfinder.marketing.controllers.dto.request.NewsRequest;
import com.roomfinder.marketing.controllers.dto.response.NewsResponse;
import com.roomfinder.marketing.services.NewsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NewsFacade {
    // News about products, markets, and programs are available on the web.
    // The NewsFacade class provides methods to create, read, update, and delete.
    // createNews
    NewsService newsService;

    public NewsResponse createNews(NewsRequest request) {
        return newsService.createNews(request);
    }

    // getAllNews
    public PageResponse<NewsResponse> getAllNews(int page, int size) {
        return newsService.getAllNews(page, size);
    }

    // getNewsById
    public NewsResponse getNewsById(String id) {
        return newsService.getNewsById(id);
    }

    // updateNewsById
    public NewsResponse updateNewsById(NewsRequest request, String id) {
        return newsService.updateNewsById(id, request);
    }

    // deleteNewsById
    public String deleteNewsById(String id) {
        return newsService.deleteNewsById(id);
    }
}
