package com.roomfinder.marketing.controllers;

import com.roomfinder.marketing.controllers.dto.PageResponse;
import com.roomfinder.marketing.controllers.dto.request.PromotionalRequest;
import com.roomfinder.marketing.controllers.dto.response.PromotionalResponse;
import com.roomfinder.marketing.controllers.model.GenericApiResponse;
import com.roomfinder.marketing.facade.PromotionalFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Promotional Controller", description = "API để quản lý các chương trình khuyến mãi.")
@Slf4j
@RestController
@RequestMapping("/promotional")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class PromotionalController {

    // Dependency injection for PromotionalFacade
    PromotionalFacade promotionalFacade;

    /**
     * Lấy thông tin chương trình khuyến mãi theo ID.
     *
     * @param id ID của chương trình khuyến mãi.
     * @return Chi tiết của chương trình khuyến mãi.
     */
    @Operation(summary = "Get promotional by id")
    @GetMapping("/{id}")
    public GenericApiResponse<PromotionalResponse> getPromotionalById(@PathVariable String id) {
        var result = promotionalFacade.getPromotionalById(id);
        return GenericApiResponse.success(result);
    }

    /**
     * Tạo một chương trình khuyến mãi mới.
     *
     * @param request Thông tin chương trình khuyến mãi cần tạo.
     * @return Chi tiết của chương trình khuyến mãi đã tạo.
     */
    @PostMapping("create")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    @Operation(summary = "Tạo Promotional ",security = {@SecurityRequirement(name = "bearerAuth")})
    public GenericApiResponse<PromotionalResponse> createPromotional(@RequestBody PromotionalRequest request) {
        var result = promotionalFacade.createPromotional(request);
        return GenericApiResponse.success(result);
    }

    /**
     * Lấy danh sách các chương trình khuyến mãi.
     *
     * @param page Số trang cần lấy (mặc định là trang 1).
     * @param size Số lượng chương trình khuyến mãi mỗi trang (mặc định là 10).
     * @return Danh sách các chương trình khuyến mãi.
     */
    @GetMapping("/list-promotional")
    public GenericApiResponse<PageResponse<PromotionalResponse>> getListPromotional(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        var result = promotionalFacade.getListPromotional(page, size);
        return GenericApiResponse.success(result);
    }

    /**
     * Xóa một chương trình khuyến mãi theo ID.
     *
     * @param id ID của chương trình khuyến mãi cần xóa.
     * @return Thông báo kết quả xóa chương trình khuyến mãi.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    @Operation(summary = "Xóa Promotional ",security = {@SecurityRequirement(name = "bearerAuth")})
    public GenericApiResponse<String> deletePromotional(@PathVariable(value = "id") String id) {
        var result = promotionalFacade.deletePromotional(id);
        return GenericApiResponse.success(result);
    }

    /**
     * Cập nhật thông tin một chương trình khuyến mãi theo ID.
     *
     * @param id ID của chương trình khuyến mãi cần cập nhật.
     * @param request Thông tin chương trình khuyến mãi cần cập nhật.
     * @return Chi tiết chương trình khuyến mãi sau khi cập nhật.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    @Operation(summary = "Cập nhập Promotional ",security = {@SecurityRequirement(name = "bearerAuth")})
    public GenericApiResponse<PromotionalResponse> updatePromotional(@PathVariable(value = "id") String id, @RequestBody PromotionalRequest request) {
        var result = promotionalFacade.updatePromotional(id, request);
        return GenericApiResponse.success(result);
    }
}
