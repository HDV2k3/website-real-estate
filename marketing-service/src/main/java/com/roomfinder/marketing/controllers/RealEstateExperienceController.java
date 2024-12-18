package com.roomfinder.marketing.controllers;

import com.roomfinder.marketing.controllers.dto.PageResponse;
import com.roomfinder.marketing.controllers.dto.request.NewsRequest;
import com.roomfinder.marketing.controllers.dto.response.NewsResponse;
import com.roomfinder.marketing.controllers.dto.response.PostImageResponse;
import com.roomfinder.marketing.controllers.model.GenericApiResponse;
import com.roomfinder.marketing.facade.MediaFacade;
import com.roomfinder.marketing.facade.RealEstateExperienceFacade;
import com.roomfinder.marketing.repositories.entities.PostImage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

@Tag(name = "RealEstateExperience Controller", description = "Quản lý các trải nghiệm bất động sản")
@Slf4j
@RestController
@RequestMapping("/experience")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class RealEstateExperienceController {

    RealEstateExperienceFacade realEstateExperienceFacade;
    MediaFacade mediaFacade;

    /**
     * Tạo một trải nghiệm bất động sản mới
     *
     * @param request Dữ liệu để tạo trải nghiệm.
     * @return Thông tin về trải nghiệm bất động sản đã tạo.
     */
    @Operation(summary = "Tạo một trải nghiệm bất động sản", description = "Tạo một trải nghiệm bất động sản mới với dữ liệu được cung cấp",security = {@SecurityRequirement(name = "bearerAuth")})
    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    public GenericApiResponse<NewsResponse> createRealEstateExperience(@RequestBody NewsRequest request) {
        var result = realEstateExperienceFacade.createRealEstateExperience(request);
        return GenericApiResponse.success(result);
    }

    /**
     * Lấy tất cả các trải nghiệm bất động sản với phân trang.
     *
     * @param page Số trang (mặc định là 1).
     * @param size Số lượng trải nghiệm mỗi trang (mặc định là 10).
     * @return Danh sách trải nghiệm bất động sản với phân trang.
     */
    @Operation(summary = "Lấy danh sách trải nghiệm bất động sản", description = "Lấy tất cả các trải nghiệm bất động sản với phân trang",security = {@SecurityRequirement(name = "bearerAuth")})
    @GetMapping("/all")
    public GenericApiResponse<PageResponse<NewsResponse>> getAllRealEstateExperience(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        var result = realEstateExperienceFacade.getAllRealEstateExperience(page, size);
        return GenericApiResponse.success(result);
    }

    /**
     * Lấy một trải nghiệm bất động sản theo ID.
     *
     * @param id ID của trải nghiệm bất động sản.
     * @return Thông tin chi tiết về trải nghiệm.
     */
    @Operation(summary = "Lấy chi tiết trải nghiệm bất động sản theo ID", description = "Lấy thông tin chi tiết của một trải nghiệm bất động sản theo ID",security = {@SecurityRequirement(name = "bearerAuth")})
    @GetMapping("/get/{id}")
    public GenericApiResponse<NewsResponse> getRealEstateExperienceById(@PathVariable String id) {
        var result = realEstateExperienceFacade.getRealEstateExperienceById(id);
        return GenericApiResponse.success(result);
    }

    /**
     * Cập nhật thông tin một trải nghiệm bất động sản.
     *
     * @param request Dữ liệu mới để cập nhật trải nghiệm.
     * @param id ID của trải nghiệm bất động sản cần cập nhật.
     * @return Thông tin về trải nghiệm bất động sản đã cập nhật.
     */
    @Operation(summary = "Cập nhật trải nghiệm bất động sản", description = "Cập nhật thông tin của một trải nghiệm bất động sản theo ID",security = {@SecurityRequirement(name = "bearerAuth")})
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    public GenericApiResponse<NewsResponse> updateRealEstateExperienceById(@RequestBody NewsRequest request, @PathVariable String id) {
        var result = realEstateExperienceFacade.updateRealEstateExperienceById(request, id);
        return GenericApiResponse.success(result);
    }

    /**
     * Xóa một trải nghiệm bất động sản theo ID.
     *
     * @param id ID của trải nghiệm bất động sản cần xóa.
     * @return Thông báo về kết quả xóa.
     */
    @Operation(summary = "Xóa một trải nghiệm bất động sản", description = "Xóa một trải nghiệm bất động sản theo ID",security = {@SecurityRequirement(name = "bearerAuth")})
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    public GenericApiResponse<String> deleteRealEstateExperienceById(@PathVariable String id) {
        var result = realEstateExperienceFacade.deleteRealEstateExperienceById(id);
        return GenericApiResponse.success(result);
    }

    /**
     * Tải lên hình ảnh cho một trải nghiệm bất động sản.
     *
     * @param id ID của trải nghiệm bất động sản cần tải lên hình ảnh.
     * @param files Các hình ảnh cần tải lên.
     * @return Danh sách các hình ảnh đã tải lên.
     */
    @Operation(summary = "Tải lên hình ảnh cho trải nghiệm bất động sản", description = "Tải lên hình ảnh cho một trải nghiệm bất động sản theo ID",security = {@SecurityRequirement(name = "bearerAuth")})
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    @PostMapping(value = "/upload-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tải lên hình ảnh thành công"),
            @ApiResponse(responseCode = "400", description = "Lỗi tải hình ảnh")
    })
    public GenericApiResponse<Set<PostImageResponse>> uploadImagesRealEstateEx(
            @RequestParam String id, @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        Set<PostImage> uploadedImages = mediaFacade.uploadImagesRealEstateExperience(id, files);
        Set<PostImageResponse> responseImages = uploadedImages.stream()
                .map(image -> new PostImageResponse(image.getName(), image.getType(), image.getUrlImagePost()))
                .collect(Collectors.toSet());
        return GenericApiResponse.success(responseImages);
    }
}

