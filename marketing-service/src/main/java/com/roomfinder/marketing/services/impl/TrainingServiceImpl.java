package com.roomfinder.marketing.services.impl;

import com.roomfinder.marketing.controllers.dto.*;
import com.roomfinder.marketing.controllers.dto.response.FeeDetailResponse;
import com.roomfinder.marketing.repositories.RoomSalePostRepository;
import com.roomfinder.marketing.repositories.entities.RoomInfo;
import com.roomfinder.marketing.repositories.entities.RoomSalePostEntity;
import com.roomfinder.marketing.services.TrainingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TrainingServiceImpl implements TrainingService {
    RoomSalePostRepository roomSalePostRepository;

    @Override
    public GenericResponseAI getRoomIntents() {
        List<String> roomNames = getAllRoomNames();

        GenericResponseAI.Intent intent = new GenericResponseAI.Intent();
        intent.setTag("Danh sách các loại phòng");
        intent.setPatterns(List.of(
                "Có những loại phòng nào?",
                "Hãy cho tôi biết các loại phòng",
                "Danh sách các phòng hiện có",
                "Liệt kê tất cả các loại phòng"
        ));
        intent.setResponses(roomNames.stream()
                .map(name -> "Phòng: " + name)
                .collect(Collectors.toList()));

        GenericResponseAI response = new GenericResponseAI();
        response.setIntents(List.of(intent));

        return response;
    }


    @Override
    public GenericResponseAI getRoomIntentsWithAddress() {
        List<RoomWithAddressDto> roomNamesWithAddresses = getAllRoomNamesWithAddress();

        List<GenericResponseAI.Intent> intents = roomNamesWithAddresses.stream()
                .map(room -> {
                    GenericResponseAI.Intent intent = new GenericResponseAI.Intent();
                    intent.setTag("Địa chỉ phòng " + room.getName());
                    intent.setPatterns(List.of(
                            room.getName() + " địa chỉ ở đâu?",
                            "Địa chỉ của phòng " + room.getName() + " là gì?",
                            "Tôi muốn biết địa chỉ phòng " + room.getName()
                    ));
                    intent.setResponses(List.of(
                            room.getName() + " - Địa chỉ: " + room.getAddress()
                    ));
                    return intent;
                })
                .collect(Collectors.toList());

        GenericResponseAI response = new GenericResponseAI();
        response.setIntents(intents);

        return response;
    }


    @Override
    public GenericResponseAI getRoomIntentsStatus() {
        List<RoomWithStatusDto> roomWithStatusDtos = getAllRoomNamesWithStatus();

        List<GenericResponseAI.Intent> intents = roomWithStatusDtos.stream()
                .map(roomWithStatusDto -> {
                    GenericResponseAI.Intent intent = new GenericResponseAI.Intent();
                    intent.setTag("Trạng thái phòng " + roomWithStatusDto.getName());
                    intent.setPatterns(List.of(
                            roomWithStatusDto.getName() + " còn phòng không?",
                            "Phòng " + roomWithStatusDto.getName() + " có còn trống không?",
                            "Trạng thái của phòng " + roomWithStatusDto.getName()
                    ));
                    intent.setResponses(List.of(
                            roomWithStatusDto.getName() + " - Trạng thái: " + roomWithStatusDto.getStatus()
                    ));
                    return intent;
                })
                .collect(Collectors.toList());

        GenericResponseAI response = new GenericResponseAI();
        response.setIntents(intents);

        return response;
    }


    @Override
    public GenericResponseAI getRoomIntentsTotalArea() {
        List<RoomWithTotalArea> roomWithTotalAreas = getAllRoomNamesWithTotalArea();

        List<GenericResponseAI.Intent> intents = roomWithTotalAreas.stream()
                .map(roomWithTotalArea -> {
                    GenericResponseAI.Intent intent = new GenericResponseAI.Intent();
                    intent.setTag("Diện tích phòng " + roomWithTotalArea.getName());
                    intent.setPatterns(List.of(
                            roomWithTotalArea.getName() + " diện tích như thế nào?",
                            "Diện tích phòng " + roomWithTotalArea.getName() + " là bao nhiêu?",
                            "Kích thước phòng " + roomWithTotalArea.getName()
                    ));
                    intent.setResponses(List.of(
                            roomWithTotalArea.getName() + " - có chiều dài: " + roomWithTotalArea.getHeight() +
                                    "m, chiều rộng: " + roomWithTotalArea.getWidth() + "m, diện tích tổng: " + roomWithTotalArea.getTotalArea() + "m²"
                    ));
                    return intent;
                })
                .collect(Collectors.toList());

        GenericResponseAI responseAI = new GenericResponseAI();
        responseAI.setIntents(intents);

        return responseAI;
    }


    @Override
    public GenericResponseAI getRoomIntentsInfoUser() {
        List<RoomWithInfoUser> roomWithInfoUsers = getAllRoomNamesWithInfoUser();

        List<GenericResponseAI.Intent> intents = roomWithInfoUsers.stream()
                .map(roomWithInfoUser -> {
                    GenericResponseAI.Intent intent = new GenericResponseAI.Intent();
                    intent.setTag("Thông tin chủ phòng " + roomWithInfoUser.getName());
                    intent.setPatterns(List.of(
                            "Cho tôi thông tin chủ phòng " + roomWithInfoUser.getName(),
                            "Ai là người quản lý phòng " + roomWithInfoUser.getName() + "?",
                            "Thông tin liên lạc chủ phòng " + roomWithInfoUser.getName()
                    ));
                    intent.setResponses(List.of(
                            "Phòng " + roomWithInfoUser.getName() + " do ông " + roomWithInfoUser.getCreatedBy() + " làm đại diện. " +
                                    "Số điện thoại: " + roomWithInfoUser.getContactInfo()
                    ));
                    return intent;
                })
                .collect(Collectors.toList());

        GenericResponseAI genericResponseAI = new GenericResponseAI();
        genericResponseAI.setIntents(intents);

        return genericResponseAI;
    }


    @Override
    public GenericResponseAI getRoomIntentsUtility() {
        List<RoomUtilityDto> roomUtilityDtos = getAllRoomNamesWithUtility();

        List<GenericResponseAI.Intent> intents = roomUtilityDtos.stream()
                .map(RoomUtilityDto -> {
                    GenericResponseAI.Intent intent = new GenericResponseAI.Intent();
                    intent.setTag("Tiện ích phòng " + RoomUtilityDto.getName());
                    intent.setPatterns(List.of(
                            "Cho tôi thông tin tiện ích của phòng " + RoomUtilityDto.getName(),
                            "Phòng " + RoomUtilityDto.getName() + " có tiện ích gì?",
                            "Tiện ích phòng " + RoomUtilityDto.getName()
                    ));
                    intent.setResponses(List.of(
                            "Phòng " + RoomUtilityDto.getName() + " có các tiện ích: " +
                                    RoomUtilityDto.getAmenitiesAvailability() + ", nội thất: " + RoomUtilityDto.getFurnitureAvailability()
                    ));
                    return intent;
                })
                .collect(Collectors.toList());

        GenericResponseAI genericResponseAI = new GenericResponseAI();
        genericResponseAI.setIntents(intents);

        return genericResponseAI;
    }


    @Override
    public GenericResponseAI getRoomIntentsPricingDetails() {
        List<PricingDetailsDto> pricingDetailsDtos = getAllRoomNamesWithPricingDetails();

        List<GenericResponseAI.Intent> intents = pricingDetailsDtos.stream().map(
                PricingDetailsDto -> {
                    String additionalFees = PricingDetailsDto.getAdditionalFees().stream()
                            .map(fee -> "Loại phí: " + fee.getType() + ", Số tiền: " + fee.getAmount() + " VNĐ")
                            .collect(Collectors.joining("; "));

                    GenericResponseAI.Intent intent = new GenericResponseAI.Intent();
                    intent.setTag("Chi tiết phí phòng " + PricingDetailsDto.getName());
                    intent.setPatterns(List.of(
                            "Cho tôi thông tin các loại phí của phòng " + PricingDetailsDto.getName(),
                            "Phòng " + PricingDetailsDto.getName() + " có các loại phí gì?",
                            "Thông tin giá cả phòng " + PricingDetailsDto.getName()
                    ));
                    intent.setResponses(List.of(
                            "Phòng " + PricingDetailsDto.getName() + " có các loại phí như sau: " +
                                    "Phí nhà hàng tháng: " + PricingDetailsDto.getBasePrice() + " VNĐ, " +
                                    "Phí nước sinh hoạt: " + PricingDetailsDto.getWaterCost() + " VNĐ, " +
                                    "Phí điện: " + PricingDetailsDto.getElectricityCost() + " VNĐ, " +
                                    "Các phí bổ sung: " + additionalFees
                    ));
                    return intent;
                }
        ).collect(Collectors.toList());

        GenericResponseAI genericResponseAI = new GenericResponseAI();
        genericResponseAI.setIntents(intents);

        return genericResponseAI;
    }


    public List<PricingDetailsDto> getAllRoomNamesWithPricingDetails() {
        return roomSalePostRepository.findAll().stream()
                .map(entity -> new PricingDetailsDto(
                        entity.getRoomInfo().getName(), // Lấy tên phòng
                        entity.getPricingDetails().getBasePrice(), // Giá cơ bản
                        entity.getPricingDetails().getElectricityCost(), // Giá điện
                        entity.getPricingDetails().getWaterCost(), // Giá nước
                        entity.getPricingDetails().getAdditionalFees().stream()
                                .map(feeDetail -> new FeeDetailResponse(
                                        feeDetail.getType(), // Loại phí
                                        feeDetail.getAmount() // Số tiền phí
                                ))
                                .collect(Collectors.toList()) // Chuyển thành danh sách FeeDetailResponse
                ))
                .distinct() // Loại bỏ các đối tượng trùng lặp
                .collect(Collectors.toList()); // Chuyển thành danh sách PricingDetailsDto
    }

    public List<RoomUtilityDto> getAllRoomNamesWithUtility() {
        return roomSalePostRepository.findAll().stream()
                .map(entity -> new RoomUtilityDto(
                        entity.getRoomInfo().getName(),
                        entity.getRoomUtility().getAmenitiesAvailability(),
                        entity.getRoomUtility().getFurnitureAvailability()
                ))
                .distinct()
                .collect(Collectors.toList());
    }
    public List<RoomWithInfoUser> getAllRoomNamesWithInfoUser() {
        return roomSalePostRepository.findAll().stream()
                .map(entity -> new RoomWithInfoUser(
                        entity.getRoomInfo().getName(),
                        entity.getCreatedBy(),
                        entity.getContactInfo()
                ))
                .distinct()
                .collect(Collectors.toList());
    }

    public List<RoomWithTotalArea> getAllRoomNamesWithTotalArea() {
        return roomSalePostRepository.findAll().stream()
                .map(RoomSalePostEntity::getRoomInfo)
                .map(roomInfo -> new RoomWithTotalArea(roomInfo.getName(), roomInfo.getTotalArea(), roomInfo.getWidth(), roomInfo.getHeight()))
                .distinct()
                .collect(Collectors.toList());
    }

    public List<RoomWithAddressDto> getAllRoomNamesWithAddress() {
        return roomSalePostRepository.findAll().stream()
                .map(RoomSalePostEntity::getRoomInfo)
                .map(roomInfo -> new RoomWithAddressDto(roomInfo.getName(), roomInfo.getAddress()))
                .distinct()
                .collect(Collectors.toList());
    }

    public List<RoomWithStatusDto> getAllRoomNamesWithStatus() {
        return roomSalePostRepository.findAll().stream()
                .map(entity -> new RoomWithStatusDto(
                        entity.getRoomInfo().getName(), // Lấy name từ RoomInfo
                        entity.getStatus()              // Lấy status từ RoomSalePostEntity
                ))
                .distinct()
                .collect(Collectors.toList());
    }


    public List<String> getAllRoomNames() {
        return roomSalePostRepository.findAll().stream()
                .map(RoomSalePostEntity::getRoomInfo)
                .map(RoomInfo::getName)
                .distinct()
                .collect(Collectors.toList());
    }
}
