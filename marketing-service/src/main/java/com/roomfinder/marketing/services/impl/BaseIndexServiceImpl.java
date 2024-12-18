package com.roomfinder.marketing.services.impl;

import com.roomfinder.marketing.controllers.dto.PageResponse;
import com.roomfinder.marketing.controllers.dto.request.BaseIndexRequest;
import com.roomfinder.marketing.controllers.dto.response.BaseIndexResponse;
import com.roomfinder.marketing.controllers.dto.response.RoomSalePostResponse;
import com.roomfinder.marketing.exception.AppException;
import com.roomfinder.marketing.exception.ErrorCode;
import com.roomfinder.marketing.mappers.BaseIndexMapper;
import com.roomfinder.marketing.mappers.MarketingMapper;
import com.roomfinder.marketing.repositories.FeaturedRepository;
import com.roomfinder.marketing.repositories.RoomSalePostRepository;
import com.roomfinder.marketing.repositories.UserRepository;
import com.roomfinder.marketing.repositories.clients.PaymentClient;
import com.roomfinder.marketing.repositories.entities.FeaturedRoomEntity;
import com.roomfinder.marketing.services.BaseIndexService;
import com.roomfinder.marketing.services.helper.CalculateExpiry;
import com.roomfinder.marketing.services.helper.GetTimeExpiry;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BaseIndexServiceImpl implements BaseIndexService {
    FeaturedRepository featuredRepository;
    BaseIndexMapper baseIndexMapper;
    MarketingMapper marketingMapper;
    RoomSalePostRepository roomSalePostRepository;
    CalculateExpiry calculateExpiry;
    UserRepository userRepository;
    GetTimeExpiry getTimeExpiry;
    PaymentClient paymentClient;
    @Override
    public RoomSalePostResponse createFeaturedAdsFee(int typePackage, String roomId) {
        // Kiểm tra xác thực người dùng
        var user = userRepository.getMyInfo();
        // Tìm phòng trong kho dữ liệu
        var room = roomSalePostRepository.findByRoomId(roomId)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));

        var featured = featuredRepository.findByRoomId(roomId);
        if (featured.isEmpty()) {
            // Kiểm tra tính hợp lệ của gói
            if (typePackage < 1 || typePackage > 3) {
                throw new IllegalArgumentException("Type must be 1, 2, or 3");
            }
            // Lấy thông tin Featured Room nếu tồn tại
            FeaturedRoomEntity featuredRoomEntity = featuredRepository.findByRoomId(roomId)
                    .orElseGet(() -> {
                        // Nếu chưa tồn tại, tạo mới Featured Room
                        var newEntity = new FeaturedRoomEntity();
                        newEntity.setRoomId(roomId);
                        newEntity.setExpiry(calculateExpiry.calculateExpiryFeatured(typePackage));
                        Optional<FeaturedRoomEntity> lastFeaturedRoom = featuredRepository.findFirstByOrderByIndexDesc();
                        if (lastFeaturedRoom.isPresent()) {
                            Integer currentMaxIndex = lastFeaturedRoom.get().getIndex();
                            newEntity.setIndex(currentMaxIndex + 1);
                        } else {
                            newEntity.setIndex(1);
                        }
                        // Sử dụng LinkedList thay vì ArrayList
                        LinkedList<Integer> initialTypes = new LinkedList<>();
                        initialTypes.addFirst(typePackage); // Thêm type đầu tiên vào đầu danh sách
                        newEntity.setTypes(initialTypes);

                        newEntity.setUserId(user.getId());
                        return newEntity;
                    });
            featuredRepository.save(featuredRoomEntity);
            paymentClient.minusBalance(typePackage,roomId);
            // Lưu cập nhật vào cơ sở dữ liệu
        } else {
            // Cập nhật Featured Room đã tồn tại
            FeaturedRoomEntity existingFeatured = featured.get();

            // Thêm type mới vào cuối danh sách
            LinkedList<Integer> existingTypes = new LinkedList<>(existingFeatured.getTypes());
            existingTypes.addLast(typePackage);
            existingFeatured.setTypes(existingTypes);

            // Cộng thêm thời gian hết hạn mới vào thời gian hiện tại
            Instant newExpiry = calculateExpiry.calculateExpiryFromType(typePackage, existingFeatured.getExpiry());
            existingFeatured.setExpiry(newExpiry);
            featuredRepository.save(existingFeatured);
            paymentClient.minusBalance(typePackage,roomId);
        }

        // Map response từ FeaturedRoomEntity và Room
        RoomSalePostResponse response = marketingMapper.toResponseRoomSalePost(room);

        // Lấy index từ FeaturedRoomEntity
        FeaturedRoomEntity featuredRoomEntity = featured.isPresent() ? featured.get() : null;
        if (featuredRoomEntity != null) {
            // Set index
            response.setIndex(featuredRoomEntity.getIndex());

            // Tính toán remainingFeaturedTime (thời gian còn lại)
            long remainingSeconds = ChronoUnit.SECONDS.between(Instant.now(), featuredRoomEntity.getExpiry());
            response.setRemainingFeaturedTime(remainingSeconds);

            // Format thời gian còn lại
            String formattedRemainingTime = getTimeExpiry.formatRemainingTime(remainingSeconds);
            response.setRemainingFeaturedTimeFormatted(formattedRemainingTime);
        }

        return response;
    }



    @Override
    public BaseIndexResponse updateFeatured(String id, BaseIndexRequest featuredRequest) {
        return featuredRepository.findById(id)
                .map(existingFeatured -> {
                    baseIndexMapper.updateFeaturedRoom(featuredRequest, existingFeatured);
                    featuredRepository.save(existingFeatured);
                    return baseIndexMapper.toFeatureResponse(existingFeatured);
                })
                .orElseThrow(() -> new AppException(ErrorCode.FEATURED_UPDATE_FAILED));
    }

    @Override
    public void deleteFeatured(String id) {
        featuredRepository.findById(id)
                .ifPresentOrElse(featuredRepository::delete,
                        () -> {
                            throw new AppException(ErrorCode.FEATURED_DELETION_FAILED);
                        });
    }

    @Override
    public BaseIndexResponse getFeaturedById(String id) {
        var featuredRoomEntity = featuredRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.FEATURED_ALREADY_EXISTS));
        return baseIndexMapper.toFeatureResponse(featuredRoomEntity);
    }

    @Override
    public PageResponse<BaseIndexResponse> getFeatures(int page, int size) {
        Sort sort = Sort.by("index").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        var pageData = featuredRepository.findAll(pageable);

        List<BaseIndexResponse> responses = pageData.getContent().stream()
                .map(baseIndexMapper::toFeatureResponse)
                .toList();
        return PageResponse.<BaseIndexResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(responses)
                .build();
    }


}
