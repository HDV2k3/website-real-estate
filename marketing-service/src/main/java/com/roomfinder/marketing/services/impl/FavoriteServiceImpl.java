package com.roomfinder.marketing.services.impl;
import com.roomfinder.marketing.controllers.dto.PageResponse;
import com.roomfinder.marketing.controllers.dto.request.FavoriteRequest;
import com.roomfinder.marketing.controllers.dto.response.FavoriteResponse;
import com.roomfinder.marketing.controllers.dto.response.RoomSalePostResponse;
import com.roomfinder.marketing.exception.AppException;
import com.roomfinder.marketing.exception.ErrorCode;
import com.roomfinder.marketing.mappers.FavoriteMapper;
import com.roomfinder.marketing.mappers.MarketingMapper;
import com.roomfinder.marketing.repositories.FavoriteRepository;
import com.roomfinder.marketing.repositories.PromotionalRepository;
import com.roomfinder.marketing.repositories.RoomSalePostRepository;
import com.roomfinder.marketing.repositories.UserRepository;
import com.roomfinder.marketing.repositories.entities.FavoriteEntity;
import com.roomfinder.marketing.repositories.entities.PromotionalRoomEntity;
import com.roomfinder.marketing.repositories.entities.RoomSalePostEntity;
import com.roomfinder.marketing.services.FavoriteService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FavoriteServiceImpl implements FavoriteService {
    FavoriteRepository favoriteRepository;
    UserRepository userRepository;
    FavoriteMapper favoriteMapper;
    RoomSalePostRepository roomSalePostRepository;
    MarketingMapper marketingMapper;
    PromotionalRepository promotionalRepository;
    @Override
    public FavoriteResponse createFavorite(FavoriteRequest request) {
        var user = userRepository.getMyInfo();

        Optional<FavoriteEntity> existingFavoriteRoom = favoriteRepository.findByRoomId(request.getRoomId());
        if (existingFavoriteRoom.isPresent()) {
            throw new AppException(ErrorCode.FEATURED_ALREADY_EXISTS);
        }
        var favoriteRoomEntity = favoriteMapper.toCreateFavoriteRoom(request);
        Optional<FavoriteEntity> lastFavoriteRoom = favoriteRepository.findFirstByOrderByIndexDesc();
        if (lastFavoriteRoom.isPresent()) {
            Integer currentMaxIndex = lastFavoriteRoom.get().getIndex();
            favoriteRoomEntity.setIndex(currentMaxIndex + 1);
        } else {
            favoriteRoomEntity.setIndex(1);
        }
        favoriteRoomEntity.setUserId(user.getId());
        favoriteRepository.save(favoriteRoomEntity);

        return favoriteMapper.toFavoriteResponse(favoriteRoomEntity);
    }

    @Override
    public void deleteFavorite(String id) {
        favoriteRepository.findByRoomId(id)
                .ifPresentOrElse(favoriteRepository::delete,
                        () -> {
                            throw new AppException(ErrorCode.FEATURED_DELETION_FAILED);
                        });
    }

    @Override
    public PageResponse<FavoriteResponse> getFavorite(int page, int size) {
        // Tạo đối tượng sắp xếp theo "index" giảm dần
        Sort sort = Sort.by("index").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        // Lấy thông tin người dùng
        var user = userRepository.getMyInfo();
        int userId = user.getId();

        // Lấy dữ liệu phân trang từ repository theo userId
        var pageData = favoriteRepository.findByUserId(userId, pageable);

        // Chuyển đổi từ FavoriteEntity sang FavoriteResponse
        List<FavoriteResponse> responses = pageData.getContent().stream()
                .map(favoriteEntity -> {
                    // Lấy thông tin về phòng dựa vào roomId trong FavoriteEntity
                    RoomSalePostEntity roomEntity = roomSalePostRepository.findByRoomId(favoriteEntity.getRoomId()).orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));

                    // Khởi tạo đối tượng RoomSalePostResponse nếu có phòng
                    RoomSalePostResponse roomSalePostResponse = null;
                    if (roomEntity != null) {
                        // Sử dụng mapper để chuyển từ entity sang response
                        roomSalePostResponse = marketingMapper.toResponseRoomSalePost(roomEntity);
                    }

                    // Kiểm tra xem phòng có phải là phòng khuyến mãi không và lấy fixPrice nếu có
                    Optional<PromotionalRoomEntity> promotionalRoomEntity = promotionalRepository.findByRoomId(favoriteEntity.getRoomId());
                    if (promotionalRoomEntity.isPresent()) {
                        // Lấy giá cố định fixPrice từ phòng khuyến mãi
                        BigDecimal fixPrice = promotionalRoomEntity.get().getFixPrice();

                        // Nếu có giá cố định, thêm nó vào roomSalePostResponse
                        if (roomSalePostResponse != null) {
                            roomSalePostResponse.setFixPrice(fixPrice);
                        }
                    }

                    // Trả về FavoriteResponse với thông tin phòng, yêu thích và giá cố định trong roomSalePostResponse
                    return FavoriteResponse.builder()
                            .id(favoriteEntity.getId())
                            .roomSalePostResponse(roomSalePostResponse)  // Đưa roomSalePostResponse vào FavoriteResponse
                            .userId(favoriteEntity.getUserId())
                            .index(favoriteEntity.getIndex())
                            .build();
                })
                .toList();

        // Xây dựng và trả về đối tượng PageResponse
        return PageResponse.<FavoriteResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(responses)
                .build();
    }

}
