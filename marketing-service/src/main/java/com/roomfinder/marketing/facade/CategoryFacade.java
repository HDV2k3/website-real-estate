package com.roomfinder.marketing.facade;

import com.roomfinder.marketing.controllers.dto.request.CarouselRequest;
import com.roomfinder.marketing.controllers.dto.response.CategoryResponse;
import com.roomfinder.marketing.services.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryFacade {

    CategoryService categoryService;


    // createCategory
    public CategoryResponse createdCategory(CarouselRequest request) {
        return categoryService.createCategory(request);
    }

    // updateCategory
    public CategoryResponse updateCategory(String id, CarouselRequest request) {
        return categoryService.updateCategory(id, request);
    }

    // deleteCategory
    public String deleteCategory(String id) {
        categoryService.deleteCategory(id);
        return "Delete Successful";
    }

    // getCategory
    public CategoryResponse getCategory(String id) {
        return categoryService.getCategory(id);
    }

    // getCategories
    public List<CategoryResponse> getCategories() {
        return categoryService.getCategories();
    }

    // getCategoryByName
    public CategoryResponse getCategoryByName(String name) {
        return categoryService.getCategoryByName(name);
    }
}
