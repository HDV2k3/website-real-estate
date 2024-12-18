package com.roomfinder.marketing.services.impl;

import com.roomfinder.marketing.controllers.dto.PageResponse;
import com.roomfinder.marketing.controllers.dto.request.PromotionalRequest;
import com.roomfinder.marketing.controllers.dto.response.PromotionalResponse;
import com.roomfinder.marketing.exception.AppException;
import com.roomfinder.marketing.exception.ErrorCode;
import com.roomfinder.marketing.mappers.PromotionalMapper;
import com.roomfinder.marketing.repositories.PromotionalRepository;
import com.roomfinder.marketing.repositories.entities.PromotionalRoomEntity;
import com.roomfinder.marketing.services.PromotionalService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PromotionalServiceImpl implements PromotionalService {
    PromotionalRepository promotionalRepository;
    PromotionalMapper promotionalMapper;

    @Override
    public PromotionalResponse createPromotional(PromotionalRequest promotionalRequest) {
        Optional<PromotionalRoomEntity> existingPromotionalRoom = promotionalRepository.findByRoomId(promotionalRequest.getRoomId());
        if (existingPromotionalRoom.isPresent()) {
            throw new AppException(ErrorCode.PROMOTIONAL_ALREADY_EXISTS);
        }
        var promotionalRoomEntity = promotionalMapper.toCreatePromotionalRoom(promotionalRequest);
        promotionalRepository.save(promotionalRoomEntity);

        return promotionalMapper.toPromotionalRoomResponse(promotionalRoomEntity);
    }

    @Override
    public PromotionalResponse updatePromotional(String id, PromotionalRequest promotionalRequest) {
        return promotionalRepository.findById(id)
                .map(existingPromotional -> {
                    promotionalMapper.updatePromotionalRoom(promotionalRequest, existingPromotional);
                    promotionalRepository.save(existingPromotional);
                    return promotionalMapper.toPromotionalRoomResponse(existingPromotional);
                })
                .orElseThrow(() -> new AppException(ErrorCode.PROMOTIONAL_UPDATE_FAILED));
    }

    @Override
    public void deletePromotional(String id) {
        promotionalRepository.findByRoomId(id)
                .ifPresentOrElse(promotionalRepository::delete,
                        () -> {
                            throw new AppException(ErrorCode.PROMOTIONAL_DELETION_FAILED);
                        });
    }

    @Override
    public PromotionalResponse getPromotionalById(String id) {
        var promotionalRoomEntity = promotionalRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PROMOTIONAL_ALREADY_EXISTS));
        return promotionalMapper.toPromotionalRoomResponse(promotionalRoomEntity);
    }

    @Override
    public PageResponse<PromotionalResponse> getListPromotional(int page, int size) {
        Sort sort = Sort.by("fixPrice").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = promotionalRepository.findAll(pageable);
        List<PromotionalResponse> responses = pageData.getContent().stream()
                .map(promotionalMapper::toPromotionalRoomResponse)
                .toList();
        return PageResponse.<PromotionalResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(responses)
                .build();
    }
}
