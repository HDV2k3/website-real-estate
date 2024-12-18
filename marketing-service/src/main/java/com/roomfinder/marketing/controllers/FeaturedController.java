package com.roomfinder.marketing.controllers;

import com.roomfinder.marketing.controllers.dto.PageResponse;
import com.roomfinder.marketing.controllers.dto.request.BaseIndexRequest;
import com.roomfinder.marketing.controllers.dto.response.BaseIndexResponse;
import com.roomfinder.marketing.controllers.dto.response.RoomSalePostResponse;
import com.roomfinder.marketing.controllers.model.GenericApiResponse;
import com.roomfinder.marketing.facade.BaseIndexFacade;
import com.roomfinder.marketing.repositories.entities.FeaturedRoomEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Featured Controller", description = "API để quản lý các tính năng nổi bật (featured) trong hệ thống marketing.")
@Slf4j
@RestController
@RequestMapping("/featured")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class FeaturedController {
    BaseIndexFacade featuredFacade;

    /**
     * Lấy thông tin tính năng nổi bật theo ID.
     *
     * @param id ID của tính năng nổi bật.
     * @return Chi tiết tính năng nổi bật với ID đã cho.
     */
    @Operation(summary = "Lấy tính năng nổi bật theo ID")
    @GetMapping("/{id}")
    public GenericApiResponse<BaseIndexResponse> getFeaturedById(@PathVariable String id) {
        var result = featuredFacade.getFeaturedById(id);
        return GenericApiResponse.success(result);
    }

    /**
     * Tạo một tính năng nổi bật mới.
     *
     * @param roomId Thông tin tính năng nổi bật cần tạo.
     * @return Chi tiết của tính năng nổi bật vừa tạo.
     */

    @PostMapping("/create")
//    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    @Operation(summary = "Tạo một Phòng nỗi bậc",  security = {@SecurityRequirement(name = "bearerAuth")})
    public GenericApiResponse<RoomSalePostResponse> createFeatured(@RequestParam int typePackage , @RequestParam String roomId) {
        var result = featuredFacade.createFeaturedAdsFee(typePackage,roomId);
        return GenericApiResponse.success(result);
    }

    /**
     * Lấy danh sách tính năng nổi bật với phân trang.
     *
     * @param page Số trang cần lấy (mặc định là 1).
     * @param size Kích thước trang (mặc định là 10).
     * @return Danh sách các tính năng nổi bật với phân trang.
     */
    @GetMapping("/list-featured")
    public GenericApiResponse<PageResponse<BaseIndexResponse>> getFeatures(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        var result = featuredFacade.getFeatures(page, size);
        return GenericApiResponse.success(result);
    }

    /**
     * Xóa tính năng nổi bật theo ID.
     *
     * @param id ID của tính năng nổi bật cần xóa.
     * @return Thông báo kết quả xóa tính năng nổi bật.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    @Operation(summary = "Xóa một tính năng nổi bật theo ID",  security = {@SecurityRequirement(name = "bearerAuth")})
    public GenericApiResponse<String> deleteFeatured(@PathVariable(value = "id") String id) {
        var result = featuredFacade.deleteFeatured(id);
        return GenericApiResponse.success(result);
    }

    /**
     * Cập nhật một tính năng nổi bật theo ID.
     *
     * @param id ID của tính năng nổi bật cần cập nhật.
     * @param request Thông tin tính năng nổi bật mới.
     * @return Chi tiết của tính năng nổi bật đã cập nhật.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    @Operation(summary = "Cập nhật một tính năng nổi bật theo ID",  security = {@SecurityRequirement(name = "bearerAuth")})
    public GenericApiResponse<BaseIndexResponse> updateFeatured(@PathVariable(value = "id") String id, @RequestBody BaseIndexRequest request) {
        var result = featuredFacade.updateFeatured(id, request);
        return GenericApiResponse.success(result);
    }
}
