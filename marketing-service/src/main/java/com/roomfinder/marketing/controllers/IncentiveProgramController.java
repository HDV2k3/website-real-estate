package com.roomfinder.marketing.controllers;

import com.roomfinder.marketing.controllers.dto.request.IncentiveProgramRequest;
import com.roomfinder.marketing.controllers.dto.response.IncentiveProgramResponse;
import com.roomfinder.marketing.controllers.dto.response.PostImageResponse;
import com.roomfinder.marketing.controllers.model.GenericApiResponse;
import com.roomfinder.marketing.facade.IncentiveProgramFacade;
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

@Tag(name = "Incentive Program Controller", description = "API để quản lý các chương trình khuyến mãi (Incentive Programs).")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/incentive-program")

public class IncentiveProgramController {

    IncentiveProgramFacade incentiveProgramFacade;
    MediaFacade mediaFacade;

    /**
     * Tạo một chương trình khuyến mãi mới.
     *
     * @param request Thông tin của chương trình khuyến mãi cần tạo.
     * @return Chi tiết của chương trình khuyến mãi đã được tạo.
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    @Operation(summary = "Tạo chương trình khuyến mãi",security = {@SecurityRequirement(name = "bearerAuth")})
    public GenericApiResponse<IncentiveProgramResponse> createIncentiveProgram(@RequestBody IncentiveProgramRequest request) {
        return GenericApiResponse.success(incentiveProgramFacade.createIncentiveProgram(request));
    }

    /**
     * Cập nhật thông tin một chương trình khuyến mãi.
     *
     * @param id ID của chương trình khuyến mãi cần cập nhật.
     * @param request Thông tin mới để cập nhật.
     * @return Chi tiết của chương trình khuyến mãi đã được cập nhật.
     */
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    @Operation(summary = "Cập nhật chương trình khuyến mãi",security = {@SecurityRequirement(name = "bearerAuth")})
    public GenericApiResponse<IncentiveProgramResponse> updateIncentiveProgram(@PathVariable String id, @RequestBody IncentiveProgramRequest request) {
        return GenericApiResponse.success(incentiveProgramFacade.updateIncentiveProgram(id, request));
    }

    /**
     * Lấy thông tin chi tiết một chương trình khuyến mãi theo ID.
     *
     * @param id ID của chương trình khuyến mãi.
     * @return Chi tiết của chương trình khuyến mãi với ID đã cho.
     */
    @GetMapping("/get/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    @Operation(summary = "Lấy chương trình khuyến mãi theo ID",security = {@SecurityRequirement(name = "bearerAuth")})
    public GenericApiResponse<IncentiveProgramResponse> getIncentiveProgram(@PathVariable String id) {
        return GenericApiResponse.success(incentiveProgramFacade.getIncentiveProgram(id));
    }

    /**
     * Lấy chương trình khuyến mãi theo trạng thái.
     *
     * @param status Trạng thái của chương trình khuyến mãi (ví dụ: "active", "inactive").
     * @return Chi tiết của chương trình khuyến mãi theo trạng thái.
     */
    @GetMapping("/get-by-status/{status}")
    @Operation(summary = "Lấy chương trình khuyến mãi theo trạng thái")
    public GenericApiResponse<IncentiveProgramResponse> getIncentiveProgramByStatus(@PathVariable String status) {
        return GenericApiResponse.success(incentiveProgramFacade.getIncentiveProgramByStatus(status));
    }

    /**
     * Xóa một chương trình khuyến mãi theo ID.
     *
     * @param id ID của chương trình khuyến mãi cần xóa.
     * @return Thông báo kết quả xóa chương trình khuyến mãi.
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    @Operation(summary = "Xóa chương trình khuyến mãi",security = {@SecurityRequirement(name = "bearerAuth")})
    public GenericApiResponse<String> deleteIncentiveProgram(@PathVariable String id) {
        return GenericApiResponse.success(incentiveProgramFacade.deleteIncentiveProgram(id));
    }

    /**
     * Lấy danh sách tất cả các chương trình khuyến mãi.
     *
     * @return Danh sách các chương trình khuyến mãi.
     */
    @GetMapping("/get-all")
    @Operation(summary = "Lấy tất cả các chương trình khuyến mãi")
    public GenericApiResponse<List<IncentiveProgramResponse>> getIncentivePrograms() {
        return GenericApiResponse.success(incentiveProgramFacade.getAllIncentivePrograms());
    }

    /**
     * Tải lên hình ảnh cho chương trình khuyến mãi theo ID.
     *
     * @param id ID của chương trình khuyến mãi.
     * @param files Các tệp hình ảnh cần tải lên.
     * @return Danh sách các hình ảnh đã tải lên cho chương trình khuyến mãi.
     */
    @PostMapping(value = "/upload-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    @Operation(summary = "Tải lên hình ảnh cho chương trình khuyến mãi theo ID",security = {@SecurityRequirement(name = "bearerAuth")})
    public GenericApiResponse<Set<PostImageResponse>> uploadPostImagesCarousel(@RequestParam String id, @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        Set<PostImage> uploadedImages = mediaFacade.uploadImagesIncentiveProgram(id, files);
        Set<PostImageResponse> responseImages = uploadedImages.stream()
                .map(image -> new PostImageResponse(image.getName(), image.getType(), image.getUrlImagePost()))
                .collect(Collectors.toSet());
        return GenericApiResponse.success(responseImages);
    }
}
