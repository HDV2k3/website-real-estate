package com.roomfinder.marketing.controllers;

import com.roomfinder.marketing.controllers.dto.request.CarouselRequest;
import com.roomfinder.marketing.controllers.dto.response.CategoryResponse;
import com.roomfinder.marketing.controllers.dto.response.PostImageResponse;
import com.roomfinder.marketing.controllers.model.GenericApiResponse;
import com.roomfinder.marketing.facade.CategoryFacade;
import com.roomfinder.marketing.facade.MediaFacade;
import com.roomfinder.marketing.repositories.entities.PostImage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "Category Controller", description = "API để quản lý các danh mục (category) trong hệ thống.")
@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class CategoryController {
    CategoryFacade categoryFacade;
    MediaFacade mediaFacade;

    /**
     * Tạo mới một danh mục.
     *
     * @param request thông tin danh mục cần tạo.
     * @return Chi tiết của danh mục vừa tạo.
     */
    @PostMapping("/created-category")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    @Operation(summary = "Tạo một danh mục",  security = {@SecurityRequirement(name = "bearerAuth")})
    public GenericApiResponse<CategoryResponse> created(@RequestBody CarouselRequest request) {
        return GenericApiResponse.success(categoryFacade.createdCategory(request));
    }

    /**
     * Cập nhật thông tin một danh mục theo ID.
     *
     * @param request thông tin danh mục cần cập nhật.
     * @param id ID của danh mục cần cập nhật.
     * @return Chi tiết của danh mục đã được cập nhật.
     */
    @PutMapping("/update-category/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    @Operation(summary = "Cập nhật một danh mục theo ID",  security = {@SecurityRequirement(name = "bearerAuth")})
    public GenericApiResponse<CategoryResponse> update(@RequestBody CarouselRequest request, @PathVariable(value = "id") String id) {
        return GenericApiResponse.success(categoryFacade.updateCategory(id, request));
    }

    /**
     * Xóa một danh mục theo ID.
     *
     * @param id ID của danh mục cần xóa.
     * @return Thông báo kết quả xóa danh mục.
     */
    @DeleteMapping("/delete-category/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    @Operation(summary = "Xóa một danh mục theo ID",  security = {@SecurityRequirement(name = "bearerAuth")})
    public GenericApiResponse<String> delete(@PathVariable(value = "id") String id) {
        return GenericApiResponse.success(categoryFacade.deleteCategory(id));
    }

    /**
     * Lấy chi tiết thông tin một danh mục theo ID.
     *
     * @param id ID của danh mục cần lấy.
     * @return Chi tiết của danh mục.
     */
    @GetMapping("/get-category/{id}")
    @Operation(summary = "Lấy thông tin một danh mục theo ID")
    public GenericApiResponse<CategoryResponse> getCategory(@PathVariable(value = "id") String id) {
        return GenericApiResponse.success(categoryFacade.getCategory(id));
    }

    /**
     * Lấy danh sách tất cả các danh mục.
     *
     * @return Danh sách các danh mục trong hệ thống.
     */
    @GetMapping("/get-categories")
    @Operation(summary = "Lấy tất cả các danh mục")
    public GenericApiResponse<List<CategoryResponse>> getCategories() {
        return GenericApiResponse.success(categoryFacade.getCategories());
    }

    /**
     * Lấy thông tin một danh mục theo tên.
     *
     * @param name tên của danh mục cần lấy.
     * @return Chi tiết danh mục theo tên.
     */
    @GetMapping("/get-category-by-name/{name}")
    @Operation(summary = "Lấy thông tin một danh mục theo tên")
    public GenericApiResponse<CategoryResponse> getCategoryByName(@PathVariable(value = "name") String name) {
        return GenericApiResponse.success(categoryFacade.getCategoryByName(name));
    }

    /**
     * Tải lên hình ảnh cho danh mục theo ID.
     *
     * @param id ID của danh mục cần tải hình ảnh lên.
     * @param files danh sách các hình ảnh cần tải lên.
     * @return Danh sách các hình ảnh đã tải lên.
     */
    @PostMapping(value = "/upload-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    @Operation(summary = "Tải lên hình ảnh cho danh mục theo ID",  security = {@SecurityRequirement(name = "bearerAuth")})
    public GenericApiResponse<Set<PostImageResponse>> uploadPostImagesCarousel(@RequestParam String id, @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        Set<PostImage> uploadedImages = mediaFacade.uploadImagesCategory(id, files);
        Set<PostImageResponse> responseImages = uploadedImages.stream()
                .map(image -> new PostImageResponse(image.getName(), image.getType(), image.getUrlImagePost()))
                .collect(Collectors.toSet());
        return GenericApiResponse.success(responseImages);
    }
}
