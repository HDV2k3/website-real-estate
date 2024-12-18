package com.roomfinder.marketing.controllers;

import com.roomfinder.marketing.controllers.dto.request.BannerRequest;
import com.roomfinder.marketing.controllers.dto.response.BannerResponse;
import com.roomfinder.marketing.controllers.model.GenericApiResponse;
import com.roomfinder.marketing.facade.BannerFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@Tag(name = "Banner Controller", description = "Điều khiển các thao tác liên quan đến banner trong hệ thống marketing.")
@Slf4j
@RestController
@RequestMapping("/banner")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class BannerController {

    BannerFacade bannerFacade;

    /**
     * Tạo mới một banner.
     *
     * @param request thông tin banner cần tạo
     * @return chi tiết của banner đã được tạo
     */
    @PostMapping("/created-banner")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    @Operation(
            summary = "Tạo mới một banner",
            description = "API này cho phép người dùng tạo mới một banner bằng cách cung cấp thông tin cần thiết.",
            responses = {
                    @ApiResponse(
                            description = "Banner đã được tạo thành công",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BannerResponse.class))
                    ),
                    @ApiResponse(
                            description = "Yêu cầu không hợp lệ nếu dữ liệu không đúng",
                            responseCode = "400"
                    )
            },
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    public GenericApiResponse<BannerResponse> created(@RequestBody BannerRequest request) {
        return GenericApiResponse.success(bannerFacade.createBanner(request));
    }

    /**
     * Xóa banner theo ID.
     *
     * @param id ID của banner cần xóa
     * @return thông báo kết quả xóa
     */
    @DeleteMapping("/delete-banner/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    @Operation(
            summary = "Xóa banner theo ID",
            description = "API này cho phép người dùng xóa banner bằng cách cung cấp ID của banner.",
            responses = {
                    @ApiResponse(
                            description = "Banner đã được xóa thành công",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(
                            description = "Không tìm thấy nếu banner với ID cung cấp không tồn tại",
                            responseCode = "404"
                    )
            },
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    public GenericApiResponse<String> delete(@PathVariable(value = "id") String id) {
        return GenericApiResponse.success(bannerFacade.deleteBanner(id));
    }

    /**
     * Lấy danh sách tất cả các banner.
     *
     * @return danh sách các banner
     */
    @GetMapping("/all")
    @Operation(
            summary = "Lấy tất cả các banner",
            description = "API này trả về danh sách tất cả các banner hiện có trong hệ thống.",
            responses = {
                    @ApiResponse(
                            description = "Danh sách banner đã được lấy thành công",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))
                    )
            }
    )
    public GenericApiResponse<List<BannerResponse>> getBanners() {
        return GenericApiResponse.success(bannerFacade.getBanners());
    }

    /**
     * Cập nhật một banner theo ID.
     *
     * @param request thông tin banner cần cập nhật
     * @param id ID của banner cần cập nhật
     * @return chi tiết của banner đã được cập nhật
     */
    @PutMapping("/update-banner/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    @Operation(
            summary = "Cập nhật một banner",

            description = "API này cho phép người dùng cập nhật thông tin của một banner có sẵn bằng cách cung cấp ID và thông tin mới.",

            responses = {
                    @ApiResponse(
                            description = "Banner đã được cập nhật thành công",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BannerResponse.class))
                    ),
                    @ApiResponse(
                            description = "Không tìm thấy nếu banner với ID cung cấp không tồn tại",
                            responseCode = "404"
                    )
            },
            security = {@SecurityRequirement(name = "bearerAuth")}

    )
    public GenericApiResponse<BannerResponse> update(@RequestBody BannerRequest request, @PathVariable(value = "id") String id) {
        return GenericApiResponse.success(bannerFacade.updateBanner(request, id));
    }
}
