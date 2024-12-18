package com.roomfinder.marketing.services.impl;

import com.roomfinder.marketing.constants.Status;
import com.roomfinder.marketing.controllers.dto.request.IncentiveProgramRequest;
import com.roomfinder.marketing.controllers.dto.response.IncentiveProgramResponse;
import com.roomfinder.marketing.controllers.dto.response.PostImageResponse;
import com.roomfinder.marketing.exception.AppException;
import com.roomfinder.marketing.exception.ErrorCode;
import com.roomfinder.marketing.repositories.IncentiveProgramRepository;
import com.roomfinder.marketing.repositories.entities.IncentiveProgramEntity;
import com.roomfinder.marketing.services.IncentiveProgramService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;


@Service
@AllArgsConstructor
public class IncentiveProgramServiceImpl implements IncentiveProgramService {
    IncentiveProgramRepository incentiveProgramRepository;

    @Override
    public IncentiveProgramResponse createInventiveProgram(IncentiveProgramRequest request) {
        IncentiveProgramEntity entity = IncentiveProgramEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .type(request.getType())
                .startDate(Instant.now())
                .endDate(request.getEndDate())
                .status(Status.PENDING)
                .build();
        entity = incentiveProgramRepository.save(entity);
        return IncentiveProgramResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .type(entity.getType())
                .startDate(entity.getStartDate().toString())
                .endDate(entity.getEndDate().toString())
                .status(entity.getStatus().name())
                .build();
    }

    @Override
    public IncentiveProgramResponse updateInventiveProgram(String id, IncentiveProgramRequest request) {
      return incentiveProgramRepository.findById(id)
                .map(entity -> {
                    entity.setName(request.getName());
                    entity.setDescription(request.getDescription());
                    entity.setType(request.getType());
                    entity.setEndDate(request.getEndDate());
                    entity.setStatus(Status.PENDING);
                    entity = incentiveProgramRepository.save(entity);
                    return IncentiveProgramResponse.builder()
                            .id(entity.getId())
                            .name(entity.getName())
                            .description(entity.getDescription())
                            .type(entity.getType())
                            .startDate(entity.getStartDate().toString())
                            .endDate(entity.getEndDate().toString())
                            .status(entity.getStatus().name())
                            .build();
                })
                .orElseThrow( () -> new AppException(ErrorCode.CATEGORY_UPDATE_FAILED));
    }

    @Override
    public IncentiveProgramResponse getInventiveProgram(String id) {
        return incentiveProgramRepository.findById(id)
                .map(categoryEntity -> IncentiveProgramResponse.builder()
                        .id(categoryEntity.getId())
                        .name(categoryEntity.getName())
                        .postImages(categoryEntity.getPostImages().stream()
                                .map(postImageEntity -> PostImageResponse.builder()
                                        .type(postImageEntity.getType())
                                        .urlImagePost(postImageEntity.getUrlImagePost())
                                        .name(postImageEntity.getName())
                                        .build())
                                .toList())
                        .build())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    @Override
    public IncentiveProgramResponse getInventiveProgramByStatus(String status) {
        return incentiveProgramRepository.findIncentiveProgramEntityByStatus(status)
                .map(categoryEntity -> IncentiveProgramResponse.builder()
                        .id(categoryEntity.getId())
                        .name(categoryEntity.getName())
                        .postImages(categoryEntity.getPostImages().stream()
                                .map(postImageEntity -> PostImageResponse.builder()
                                        .type(postImageEntity.getType())
                                        .urlImagePost(postImageEntity.getUrlImagePost())
                                        .name(postImageEntity.getName())
                                        .build())
                                .toList())
                        .build())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    @Override
    public void deleteInventiveProgram(String id) {
        incentiveProgramRepository.findById(id)
                .ifPresentOrElse(incentiveProgramRepository::delete,
                        () -> {
                            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
                        });
    }

    @Override
    public List<IncentiveProgramResponse> getAllInventivePrograms() {
        return incentiveProgramRepository.findAll().stream()
                .map(entity -> IncentiveProgramResponse.builder()
                        .id(entity.getId())
                        .name(entity.getName())
                        .description(entity.getDescription())
                        .type(entity.getType())
                        .startDate(entity.getStartDate().toString())
                        .endDate(entity.getEndDate().toString())
                        .status(entity.getStatus().name())
                        .postImages(entity.getPostImages().stream()
                                .map(postImageEntity -> PostImageResponse.builder()
                                        .type(postImageEntity.getType())
                                        .urlImagePost(postImageEntity.getUrlImagePost())
                                        .name(postImageEntity.getName())
                                        .build())
                                .toList())
                        .build())
                .toList();
    }
}



