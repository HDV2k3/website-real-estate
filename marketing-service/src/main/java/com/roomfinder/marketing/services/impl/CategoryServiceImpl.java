package com.roomfinder.marketing.services.impl;

import com.roomfinder.marketing.controllers.dto.request.CarouselRequest;
import com.roomfinder.marketing.controllers.dto.response.CategoryResponse;
import com.roomfinder.marketing.controllers.dto.response.PostImageResponse;
import com.roomfinder.marketing.exception.AppException;
import com.roomfinder.marketing.exception.ErrorCode;
import com.roomfinder.marketing.repositories.CategoryRepository;
import com.roomfinder.marketing.repositories.entities.CategoryEntity;
import com.roomfinder.marketing.services.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;

    @Override
    public CategoryResponse createCategory(CarouselRequest request) {

        CategoryEntity categoryEntity = CategoryEntity.builder()
                .name(request.getName())
                .build();

        categoryRepository.save(categoryEntity);
        return CategoryResponse.builder()
                .id(categoryEntity.getId())
                .name(categoryEntity.getName())
                .build();
    }

    @Override
    public CategoryResponse updateCategory(String id, CarouselRequest request) {
        return categoryRepository.findById(id)
                .map(categoryEntity -> {
                    categoryEntity.setName(request.getName());
                    categoryRepository.save(categoryEntity);
                    return CategoryResponse.builder()
                            .id(categoryEntity.getId())
                            .name(categoryEntity.getName())
                            .build();
                }).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_UPDATE_FAILED));

    }

    @Override
    public void deleteCategory(String id) {
        categoryRepository.findById(id)
                .ifPresentOrElse(categoryRepository::delete,
                        () -> {
                            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
                        });

    }

    @Override
    public CategoryResponse getCategory(String id) {
        return categoryRepository.findById(id)
                .map(categoryEntity -> CategoryResponse.builder()
                        .id(categoryEntity.getId())
                        .name(categoryEntity.getName())
                        .postImages(categoryEntity.getPostImages().stream()
                                .map(postImageEntity -> PostImageResponse.builder()
                                        .name(postImageEntity.getName())
                                        .type(postImageEntity.getType())
                                        .urlImagePost(postImageEntity.getUrlImagePost())
                                        .build())
                                .toList())
                        .build())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    @Override
    public List<CategoryResponse> getCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryEntity -> CategoryResponse.builder()
                        .id(categoryEntity.getId())
                        .name(categoryEntity.getName())
                        .postImages(categoryEntity.getPostImages().stream()
                                .map(postImageEntity -> PostImageResponse.builder()
                                        .name(postImageEntity.getName())
                                        .type(postImageEntity.getType())
                                        .urlImagePost(postImageEntity.getUrlImagePost())
                                        .build())
                                .toList())
                        .build())
                .toList();
    }

    @Override
    public CategoryResponse getCategoryByName(String name) {
        return categoryRepository.findByName(name).map(categoryEntity -> CategoryResponse.builder()
                .id(categoryEntity.getId())
                .name(categoryEntity.getName())
                .postImages(categoryEntity.getPostImages().stream()
                        .map(postImageEntity -> PostImageResponse.builder()
                                .name(postImageEntity.getName())
                                .type(postImageEntity.getType())
                                .urlImagePost(postImageEntity.getUrlImagePost())
                                .build())
                        .toList())
                .build()
        ).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
    }
}
