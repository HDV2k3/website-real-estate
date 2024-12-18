package com.roomfinder.marketing.controllers;

import com.roomfinder.marketing.controllers.dto.PageResponse;
import com.roomfinder.marketing.controllers.dto.request.FavoriteRequest;
import com.roomfinder.marketing.controllers.dto.response.FavoriteResponse;
import com.roomfinder.marketing.controllers.model.GenericApiResponse;
import com.roomfinder.marketing.facade.FavoriteFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Favorite Controller", description = "API để quản lý các bai dang duoc nguoi dung luu  trong hệ thống marketing.")
@Slf4j
@RestController
@RequestMapping("/favorite")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FavoriteController {

    FavoriteFacade favoriteFacade;
    @Operation(summary = "luu 1 bai viet trong danh sach yeu thich ",security = {@SecurityRequirement(name = "bearerAuth")})
    @PostMapping("/create")
    public GenericApiResponse<FavoriteResponse> create(@RequestBody FavoriteRequest request)
    {
        return GenericApiResponse.success(favoriteFacade.create(request));
    }
    @Operation(summary = "xoa 1 bai viet trong danh sach yeu thich",security = {@SecurityRequirement(name = "bearerAuth")})
    @DeleteMapping("/delete")
    public GenericApiResponse<String> delete(@RequestParam String roomId)
    {
        return GenericApiResponse.success(favoriteFacade.delete(roomId));
    }
    @Operation(summary = "Lay tat ca bai viet cua user da luu ",security = {@SecurityRequirement(name = "bearerAuth")})
    @GetMapping("/all")
    public GenericApiResponse<PageResponse<FavoriteResponse>> getFavorites(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size)
    {
        return GenericApiResponse.success(favoriteFacade.getFavorites(page, size));
    }
}
