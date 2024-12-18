package com.roomfinder.marketing.services.impl;

import com.roomfinder.marketing.controllers.dto.PageResponse;
import com.roomfinder.marketing.controllers.dto.request.NewsRequest;
import com.roomfinder.marketing.controllers.dto.response.NewsResponse;
import com.roomfinder.marketing.controllers.dto.response.PostImageResponse;
import com.roomfinder.marketing.exception.AppException;
import com.roomfinder.marketing.exception.ErrorCode;
import com.roomfinder.marketing.repositories.NewsRepository;
import com.roomfinder.marketing.repositories.entities.NewsEntity;
import com.roomfinder.marketing.services.NewsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class NewsServiceImpl implements NewsService {
    NewsRepository newsRepository;

    @Override
    public NewsResponse createNews(NewsRequest newsRequest) {
        NewsEntity newsEntity = NewsEntity.builder()
                .title(newsRequest.getTitle())
                .description(newsRequest.getDescription())
                .build();
        newsRepository.save(newsEntity);
        return NewsResponse.builder()
                .id(newsEntity.getId())
                .title(newsEntity.getTitle())
                .description(newsEntity.getDescription())
                .build();
    }

    @Override
    public PageResponse<NewsResponse> getAllNews(int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);
        Page<NewsEntity> newsPage = newsRepository.findAll(pageable);

        List<NewsResponse> newsResponses = newsPage.getContent().stream()
                .map(entity -> NewsResponse.builder()
                        .id(entity.getId())
                        .title(entity.getTitle())
                        .postImages(entity.getPostImages().stream()
                                .map(postImageEntity -> PostImageResponse.builder()
                                        .name(postImageEntity.getName())
                                        .type(postImageEntity.getType())
                                        .urlImagePost(postImageEntity.getUrlImagePost())
                                        .build())
                                .toList())
                        .description(entity.getDescription())
                        .build())
                .toList();

        return PageResponse.<NewsResponse>builder()
                .currentPage(page)
                .data(newsResponses)
                .pageSize(size)
                .totalElements(newsPage.getTotalElements())
                .totalPages(newsPage.getTotalPages())
                .build();
    }

    @Override
    public NewsResponse getNewsById(String id) {
        return newsRepository.findById(id)
                .map(newsEntity -> NewsResponse.builder()
                        .id(newsEntity.getId())
                        .title(newsEntity.getTitle())
                        .description(newsEntity.getDescription())
                        .postImages(newsEntity.getPostImages().stream()
                                .map(postImageEntity -> PostImageResponse.builder()
                                        .name(postImageEntity.getName())
                                        .type(postImageEntity.getType())
                                        .urlImagePost(postImageEntity.getUrlImagePost())
                                        .build())
                                .toList())
                        .build())
                .orElseThrow(() -> new RuntimeException("Market and trend not found"));

    }

    @Override
    public NewsResponse updateNewsById(String id, NewsRequest newsRequest) {
        return newsRepository.findById(id)
                .map(newsEntity -> {
                    newsEntity.setTitle(newsRequest.getTitle());
                    newsEntity.setDescription(newsRequest.getDescription());
                    newsRepository.save(newsEntity);
                    return NewsResponse.builder()
                            .id(newsEntity.getId())
                            .title(newsEntity.getTitle())
                            .description(newsEntity.getDescription())
                            .build();
                })
                .orElseThrow(() -> new AppException(ErrorCode.NEWS_NOT_FOUND));
    }

    @Override
    public String deleteNewsById(String id) {
        newsRepository.deleteById(id);
        return "Delete Successful";
    }
}
