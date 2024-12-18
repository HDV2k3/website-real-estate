package com.roomfinder.marketing.controllers;

import com.roomfinder.marketing.controllers.dto.PageResponse;
import com.roomfinder.marketing.controllers.dto.request.FilterRequest;
import com.roomfinder.marketing.controllers.dto.request.RoomSalePostRequest;
import com.roomfinder.marketing.controllers.dto.request.SearchPostRequest;
import com.roomfinder.marketing.controllers.dto.response.InfoMarketing;
import com.roomfinder.marketing.controllers.dto.response.PostImageResponse;
import com.roomfinder.marketing.controllers.dto.response.RoomSalePostResponse;
import com.roomfinder.marketing.controllers.model.GenericApiResponse;
import com.roomfinder.marketing.facade.MarketingFacade;
import com.roomfinder.marketing.facade.MediaFacade;
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

@Tag(name = "Marketing Controller", description = "API để quản lý các bài viết về phòng cho thuê hoặc bán.")
@Slf4j
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class PostController {

    MarketingFacade marketingFacade;
    MediaFacade mediaFacade;

    /**
     * Lấy thông tin bài viết phòng cho thuê hoặc bán theo ID.
     *
     * @param id ID của bài viết phòng cần lấy thông tin.
     * @return Chi tiết bài viết phòng cho thuê hoặc bán.
     */
    @Operation(summary = "Get post by id",security = {@SecurityRequirement(name = "bearerAuth")})
    @GetMapping("/post-by-id/{id}")
    public GenericApiResponse<RoomSalePostResponse> getRoomSalePostById(@PathVariable String id) {
        var result = marketingFacade.getRoomSalePostById(id);
        return GenericApiResponse.success(result);
    }

    /**
     * Tạo một bài viết phòng cho thuê hoặc bán mới.
     *
     * @param request Thông tin của bài viết phòng cần tạo.
     * @return Chi tiết của bài viết phòng đã được tạo.
     */
    @PostMapping("/create")
    @Operation(summary = "Tạo bài viết mới ",security = {@SecurityRequirement(name = "bearerAuth")})
    public GenericApiResponse<RoomSalePostResponse> createPost(@RequestBody RoomSalePostRequest request) {
        var result = marketingFacade.createPost(request);
        return GenericApiResponse.success(result);
    }

    /**
     * Lấy danh sách các bài viết phòng cho thuê hoặc bán.
     *
     * @param page Số trang cần lấy (mặc định là trang 1).
     * @param size Số lượng bài viết mỗi trang (mặc định là 10).
     * @return Danh sách các bài viết phòng cho thuê hoặc bán.
     */
    @GetMapping("/all")
    public GenericApiResponse<PageResponse<RoomSalePostResponse>> getPosts(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        var result = marketingFacade.getPosts(page, size);
        return GenericApiResponse.success(result);
    }

    /**
     * Lấy danh sách các bài viết phòng cho thuê hoặc bán được đánh dấu là "nổi bật".
     *
     * @param page Số trang cần lấy (mặc định là trang 1).
     * @param size Số lượng bài viết mỗi trang (mặc định là 10).
     * @return Danh sách các bài viết phòng nổi bật.
     */
    @GetMapping("/list-post-featured")
    public GenericApiResponse<PageResponse<RoomSalePostResponse>> getPostsFeatured(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        var result = marketingFacade.getPostsFeatured(page, size);
        return GenericApiResponse.success(result);
    }

    /**
     * Lấy danh sách các bài viết phòng cho thuê hoặc bán mang tính chất "khuyến mãi".
     *
     * @param page Số trang cần lấy (mặc định là trang 1).
     * @param size Số lượng bài viết mỗi trang (mặc định là 10).
     * @return Danh sách các bài viết phòng khuyến mãi.
     */
    @GetMapping("/list-post-promotional")
    public GenericApiResponse<PageResponse<RoomSalePostResponse>> getPostsPromotional(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        var result = marketingFacade.getPostsPromotional(page, size);
        return GenericApiResponse.success(result);
    }

    /**
     * Xóa một bài viết phòng cho thuê hoặc bán theo ID.
     *
     * @param id ID của bài viết cần xóa.
     * @return Thông báo kết quả xóa bài viết phòng.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    @Operation(summary = "Xoas bài viết mới ",security = {@SecurityRequirement(name = "bearerAuth")})

    public GenericApiResponse<String> deletePost(@PathVariable(value = "id") String id) {
        var result = marketingFacade.deletePost(id);
        return GenericApiResponse.success(result);
    }

    /**
     * Cập nhật một bài viết phòng cho thuê hoặc bán theo ID.
     *
     * @param id ID của bài viết cần cập nhật.
     * @param request Thông tin bài viết phòng cần cập nhật.
     * @return Chi tiết bài viết phòng đã được cập nhật.
     */
    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    @Operation(summary = "Cập nhập bài viết mới ",security = {@SecurityRequirement(name = "bearerAuth")})
    public GenericApiResponse<RoomSalePostResponse> updatePost(@PathVariable(value = "id") String id, @RequestBody RoomSalePostRequest request) {
        var result = marketingFacade.updatePost(id, request);
        return GenericApiResponse.success(result);
    }

    /**
     * Tải lên hình ảnh cho bài viết phòng cho thuê hoặc bán.
     *
     * @param id ID của bài viết cần tải hình ảnh.
     * @param files Các tệp hình ảnh cần tải lên.
     * @return Danh sách các hình ảnh đã tải lên cho bài viết.
     */
    @PostMapping(value = "/upload-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    public GenericApiResponse<Set<PostImageResponse>> uploadPostImages(@RequestParam String id, @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        Set<PostImage> uploadedImages = mediaFacade.uploadImagesRoomSalePosts(id, files);
        Set<PostImageResponse> responseImages = uploadedImages.stream()
                .map(image -> new PostImageResponse(image.getName(), image.getType(), image.getUrlImagePost()))
                .collect(Collectors.toSet());
        return GenericApiResponse.success(responseImages);
    }

    /**
     * Lấy thông tin bài viết khuyến mãi theo ID.
     *
     * @param id ID của bài viết khuyến mãi cần lấy thông tin.
     * @return Chi tiết bài viết khuyến mãi.
     */
    @GetMapping("/post-promotional-by-id/{id}")
    public GenericApiResponse<RoomSalePostResponse> getPostPromotionalById(@PathVariable(value = "id") String id) {
        var result = marketingFacade.getRoomSalePostPromotionalById(id);
        return GenericApiResponse.success(result);
    }

    /**
     * Lọc các bài viết phòng cho thuê hoặc bán theo các tiêu chí.
     *
     * @param filterRequest Các tham số lọc bài viết.
     * @param page Số trang cần lấy (mặc định là trang 1).
     * @param size Số lượng bài viết mỗi trang (mặc định là 10).
     * @return Danh sách các bài viết phòng đã lọc.
     */
    @GetMapping("/post-filter")
    @Operation(summary = "Get posts by filter")
    public GenericApiResponse<PageResponse<RoomSalePostResponse>> getPostsFilter(
            @ModelAttribute FilterRequest filterRequest,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        var result = marketingFacade.getPostsFilter(filterRequest, page, size);
        return GenericApiResponse.success(result);
    }

    /**
     * Tìm kiếm các bài viết phòng cho thuê hoặc bán.
     *
     * @param searchRequest Các tham số tìm kiếm bài viết.
     * @param page Số trang cần lấy (mặc định là trang 1).
     * @param size Số lượng bài viết mỗi trang (mặc định là 10).
     * @return Danh sách các bài viết phòng tìm kiếm được.
     */
    @PostMapping("/searching")
    public GenericApiResponse<PageResponse<RoomSalePostResponse>> searchRooms(
            @RequestBody SearchPostRequest searchRequest,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size)
    {
        var results = marketingFacade.searchPosts(searchRequest, page, size);
        return GenericApiResponse.success(results);
    }
    @Operation(summary = "Lay tat ca bai viet cua user ",security = {@SecurityRequirement(name = "bearerAuth")})
    @GetMapping("/byUser")
    public GenericApiResponse<PageResponse<RoomSalePostResponse>> getPostByUser(
            @RequestParam(value = "status" ) String status,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size)
    {
        var results = marketingFacade.getPostByUser(status,page, size);
        return GenericApiResponse.success(results);
    }

    @GetMapping("/district")
    public GenericApiResponse<PageResponse<RoomSalePostResponse>> getPostByDistricts(
            @RequestParam(value = "district") int district,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size)
    {
        return GenericApiResponse.success(marketingFacade.getPostByDistrict(district,page,size));
    }

    @GetMapping("/fil-type")
    public GenericApiResponse<PageResponse<RoomSalePostResponse>> getPostByType(

            @RequestParam(value = "type") int type,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size)
    {
        return GenericApiResponse.success(marketingFacade.getPostByType(type, page, size));
    }

    @GetMapping("/info-marketing")
    public GenericApiResponse<InfoMarketing> getInfoMarketing()
    {
        return GenericApiResponse.success(marketingFacade.getInfoMarketing());
    }
}
