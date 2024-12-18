package com.roomfinder.marketing.services.impl;

import com.roomfinder.marketing.controllers.dto.PageResponse;
import com.roomfinder.marketing.controllers.dto.request.NewsRequest;
import com.roomfinder.marketing.controllers.dto.response.NewsResponse;
import com.roomfinder.marketing.controllers.dto.response.PostImageResponse;
import com.roomfinder.marketing.exception.AppException;
import com.roomfinder.marketing.exception.ErrorCode;
import com.roomfinder.marketing.repositories.RealEstateExperienceRepository;
import com.roomfinder.marketing.repositories.entities.RealEstateExperienceEntity;
import com.roomfinder.marketing.services.RealEstateExperienceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RealEstateExperienceServiceImpl implements RealEstateExperienceService {

    RealEstateExperienceRepository realEstateExperienceRepository;

    @Override
    public NewsResponse createNews(NewsRequest newsRequest) {
        RealEstateExperienceEntity newsEntity = RealEstateExperienceEntity.builder()
                .title(newsRequest.getTitle())
                .description(newsRequest.getDescription())
                .build();
        realEstateExperienceRepository.save(newsEntity);
        return NewsResponse.builder()
                .id(newsEntity.getId())
                .title(newsEntity.getTitle())
                .description(newsEntity.getDescription())
                .build();
    }

    @Override
    public PageResponse<NewsResponse> getAllNews(int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);
        Page<RealEstateExperienceEntity> newsPage = realEstateExperienceRepository.findAll(pageable);

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
        return realEstateExperienceRepository.findById(id)
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
        return realEstateExperienceRepository.findById(id)
                .map(newsEntity -> {
                    newsEntity.setTitle(newsRequest.getTitle());
                    newsEntity.setDescription(newsRequest.getDescription());
                    realEstateExperienceRepository.save(newsEntity);
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
        realEstateExperienceRepository.deleteById(id);
        return "Delete Successful";
    }
}
