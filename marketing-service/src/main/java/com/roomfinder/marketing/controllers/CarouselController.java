package com.roomfinder.marketing.controllers;

import com.roomfinder.marketing.controllers.dto.request.CarouselRequest;
import com.roomfinder.marketing.controllers.dto.response.CarouselResponse;
import com.roomfinder.marketing.controllers.dto.response.PostImageResponse;
import com.roomfinder.marketing.controllers.model.GenericApiResponse;
import com.roomfinder.marketing.facade.CarouselFacade;
import com.roomfinder.marketing.facade.MediaFacade;
import com.roomfinder.marketing.repositories.entities.PostImage;
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
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "Carousel Controller", description = "Điều khiển các thao tác liên quan đến carousel trong hệ thống marketing.")
@RequestMapping("/carousel")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class CarouselController {
    MediaFacade mediaFacade;
    CarouselFacade carouselFacade;

    /**
     * Tạo mới một carousel.
     *
     * @param request thông tin carousel cần tạo
     * @return chi tiết của carousel đã được tạo
     */
    @PostMapping("/created-carousel")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    @Operation(
            summary = "Tạo mới một carousel",
            description = "API này cho phép người dùng tạo mới một carousel bằng cách cung cấp thông tin cần thiết.",
            responses = {
                    @ApiResponse(
                            description = "Carousel đã được tạo thành công",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CarouselResponse.class))
                    ),
                    @ApiResponse(
                            description = "Yêu cầu không hợp lệ nếu dữ liệu không đúng",
                            responseCode = "400"
                    )
            },
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    public GenericApiResponse<CarouselResponse> created(@RequestBody CarouselRequest request) {
        var result = carouselFacade.created(request);
        return GenericApiResponse.success(result);
    }

    /**
     * Lấy danh sách tất cả các carousel.
     *
     * @return danh sách các carousel
     */
    @GetMapping("/all")
    @Operation(
            summary = "Lấy tất cả các carousel",
            description = "API này trả về danh sách tất cả các carousel hiện có trong hệ thống.",
            responses = {
                    @ApiResponse(
                            description = "Danh sách carousel đã được lấy thành công",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))
                    )
            }
    )
    public GenericApiResponse<List<CarouselResponse>> getCarousels() {
        var result = carouselFacade.getCarousels();
        return GenericApiResponse.success(result);
    }

    /**
     * Xóa carousel theo ID.
     *
     * @param id ID của carousel cần xóa
     * @return thông báo kết quả xóa
     */
    @DeleteMapping("/delete-carousel")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    @Operation(
            summary = "Xóa một carousel theo ID",
            description = "API này cho phép người dùng xóa carousel bằng cách cung cấp ID của carousel.",
            responses = {
                    @ApiResponse(
                            description = "Carousel đã được xóa thành công",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(
                            description = "Không tìm thấy nếu carousel với ID cung cấp không tồn tại",
                            responseCode = "404"
                    )
            },
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    public GenericApiResponse<String> delete(@PathVariable(value = "id") String id) {
        var result = carouselFacade.deleteCarousel(id);
        return GenericApiResponse.success(result);
    }

    /**
     * Tải lên hình ảnh cho một carousel.
     *
     * @param id ID của carousel cần tải lên hình ảnh
     * @param files danh sách hình ảnh cần tải lên
     * @return danh sách hình ảnh đã tải lên
     */
    @PostMapping(value = "/upload-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    @Operation(
            summary = "Tải lên hình ảnh cho carousel theo ID",
            description = "API này cho phép người dùng tải lên một hoặc nhiều hình ảnh cho carousel bằng cách cung cấp ID của carousel và danh sách hình ảnh.",
            responses = {
                    @ApiResponse(
                            description = "Hình ảnh đã được tải lên thành công",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Set.class))
                    ),
                    @ApiResponse(
                            description = "Không tìm thấy nếu carousel với ID cung cấp không tồn tại",
                            responseCode = "404"
                    )
            },
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    public GenericApiResponse<Set<PostImageResponse>> uploadPostImagesCarousel(@RequestParam String id, @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        Set<PostImage> uploadedImages = mediaFacade.uploadImagesCarousel(id, files);
        Set<PostImageResponse> responseImages = uploadedImages.stream()
                .map(image -> new PostImageResponse(image.getName(), image.getType(), image.getUrlImagePost()))
                .collect(Collectors.toSet());
        return GenericApiResponse.success(responseImages);
    }
}
