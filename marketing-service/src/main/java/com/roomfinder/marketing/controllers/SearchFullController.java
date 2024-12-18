package com.roomfinder.marketing.controllers;

import com.roomfinder.marketing.controllers.dto.PageResponse;
import com.roomfinder.marketing.controllers.dto.response.RoomSalePostResponse;
import com.roomfinder.marketing.controllers.model.GenericApiResponse;
import com.roomfinder.marketing.facade.SearchFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@Tag(name = "SearchFullController", description = "Tìm kiếm các bài đăng bán phòng hoặc nhà")
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchFullController {

    private final SearchFacade searchFacade;

    /**
     * Tìm kiếm các bài đăng bán phòng hoặc nhà theo từ khóa.
     *
     * @param searchTerm Từ khóa để tìm kiếm.
     * @param page       Số trang kết quả (mặc định là trang 1).
     * @param size       Số lượng bài đăng mỗi trang (mặc định là 10).
     * @return Kết quả tìm kiếm bao gồm thông tin bài đăng bán phòng hoặc nhà.
     */
    @Operation(
            summary = "Tìm kiếm các bài đăng bán phòng hoặc nhà theo từ khóa",
            description = "Dùng từ khóa để tìm kiếm các bài đăng bán phòng hoặc nhà, với phân trang để kiểm soát số lượng kết quả trả về."
    )
    @GetMapping("/{searchTerm}")
    public GenericApiResponse<PageResponse<RoomSalePostResponse>> searchPosts(
            @Parameter(description = "Từ khóa để tìm kiếm bài đăng bán phòng hoặc nhà") @PathVariable String searchTerm,
            @RequestParam(value = "page", required = false, defaultValue = "1") @Parameter(description = "Số trang kết quả tìm kiếm (mặc định là 1)") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") @Parameter(description = "Số lượng bài đăng mỗi trang (mặc định là 10)") int size) {

        return GenericApiResponse.success(searchFacade.searchPosts(searchTerm, page, size));
    }
}
