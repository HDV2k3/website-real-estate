package com.roomfinder.marketing.controllers;

import com.roomfinder.marketing.controllers.dto.PageResponse;
import com.roomfinder.marketing.controllers.dto.request.NewsRequest;
import com.roomfinder.marketing.controllers.dto.response.NewsResponse;
import com.roomfinder.marketing.controllers.dto.response.PostImageResponse;
import com.roomfinder.marketing.controllers.model.GenericApiResponse;
import com.roomfinder.marketing.facade.MediaFacade;
import com.roomfinder.marketing.facade.NewsFacade;
import com.roomfinder.marketing.repositories.entities.PostImage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "News Controller", description = "API để quản lý các bài viết tin tức.")
@Slf4j
@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class NewsController {

    NewsFacade newsFacade;
    MediaFacade mediaFacade;

    /**
     * Tạo một bài viết tin tức mới.
     *
     * @param request Thông tin của bài viết tin tức cần tạo.
     * @return Chi tiết của bài viết tin tức đã được tạo.
     */
    @PostMapping("/create-news")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    @Operation(summary = "Tạo bài viết tin tức",security = {@SecurityRequirement(name = "bearerAuth")})
    public GenericApiResponse<NewsResponse> createNews(@RequestBody NewsRequest request) {
        return GenericApiResponse.success(newsFacade.createNews(request));
    }

    /**
     * Lấy thông tin chi tiết một bài viết tin tức theo ID.
     *
     * @param id ID của bài viết tin tức cần lấy.
     * @return Chi tiết của bài viết tin tức với ID đã cho.
     */
    @GetMapping("/get-news/{id}")
    @Operation(summary = "Lấy bài viết tin tức theo ID")
    public GenericApiResponse<NewsResponse> getNewsById(@PathVariable String id) {
        return GenericApiResponse.success(newsFacade.getNewsById(id));
    }

    /**
     * Cập nhật thông tin một bài viết tin tức theo ID.
     *
     * @param id ID của bài viết tin tức cần cập nhật.
     * @param request Thông tin bài viết tin tức mới để cập nhật.
     * @return Chi tiết bài viết tin tức đã được cập nhật.
     */
    @PutMapping("/update-news/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    @Operation(summary = "Cập nhật bài viết tin tức theo ID",security = {@SecurityRequirement(name = "bearerAuth")})
    public GenericApiResponse<NewsResponse> updateNewsById(@RequestBody NewsRequest request, @PathVariable String id) {
        return GenericApiResponse.success(newsFacade.updateNewsById(request, id));
    }

    /**
     * Xóa bài viết tin tức theo ID.
     *
     * @param id ID của bài viết tin tức cần xóa.
     * @return Thông báo kết quả xóa bài viết tin tức.
     */
    @DeleteMapping("/delete-news/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    @Operation(summary = "Xóa bài viết tin tức theo ID",security = {@SecurityRequirement(name = "bearerAuth")})
    public GenericApiResponse<String> deleteNewsById(@PathVariable String id) {
        return GenericApiResponse.success(newsFacade.deleteNewsById(id));
    }

    /**
     * Lấy danh sách các bài viết tin tức với phân trang.
     *
     * @param page Số trang cần lấy (mặc định là trang 1).
     * @param size Số lượng bài viết tin tức trên mỗi trang (mặc định là 10).
     * @return Danh sách các bài viết tin tức.
     */
    @GetMapping("/all")
    @Operation(summary = "Lấy danh sách các bài viết tin tức")
    public GenericApiResponse<PageResponse<NewsResponse>> getAllNews(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return GenericApiResponse.success(newsFacade.getAllNews(page, size));
    }

    /**
     * Tải lên hình ảnh cho bài viết tin tức theo ID.
     *
     * @param id ID của bài viết tin tức cần tải hình ảnh.
     * @param files Các tệp hình ảnh cần tải lên.
     * @return Danh sách các hình ảnh đã tải lên cho bài viết tin tức.
     */
    @PostMapping(value = "/upload-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    @Operation(summary = "Tải lên hình ảnh cho bài viết tin tức theo ID",security = {@SecurityRequirement(name = "bearerAuth")})
    public GenericApiResponse<Set<PostImageResponse>> uploadPostImagesNews(@RequestParam String id, @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        Set<PostImage> uploadedImages = mediaFacade.uploadImagesNews(id, files);
        Set<PostImageResponse> responseImages = uploadedImages.stream()
                .map(image -> new PostImageResponse(image.getName(), image.getType(), image.getUrlImagePost()))
                .collect(Collectors.toSet());
        return GenericApiResponse.success(responseImages);
    }

}
