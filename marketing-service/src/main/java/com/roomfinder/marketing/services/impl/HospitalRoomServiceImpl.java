package com.roomfinder.marketing.services.impl;

import com.roomfinder.marketing.controllers.dto.PageResponse;
import com.roomfinder.marketing.controllers.dto.request.HospitalRoomRequest;
import com.roomfinder.marketing.controllers.dto.response.HostpitalRoomResponse;
import com.roomfinder.marketing.controllers.dto.response.RoomSalePostResponse;
import com.roomfinder.marketing.mappers.HospitalMapper;
import com.roomfinder.marketing.repositories.HospitalRoomRepository;
import com.roomfinder.marketing.repositories.entities.HospitalRoomEntity;
import com.roomfinder.marketing.services.HospitalRoomService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class HospitalRoomServiceImpl implements HospitalRoomService {

    HospitalRoomRepository hospitalRoomRepository;
    HospitalMapper hospitalMapper;
    @Override
    public HostpitalRoomResponse createHospitalRoom(HospitalRoomRequest request) {
        var hospitalRoom=  hospitalMapper.toCreate(request);
        return hospitalMapper.toResponse(hospitalRoom);
    }

    @Override
    public PageResponse<HostpitalRoomResponse> getAllHospitalRoom(int page, int size) {
        // Tạo đối tượng Pageable để phân trang
        Pageable pageable = PageRequest.of(page - 1, size);

        // Lấy dữ liệu phân trang từ repository
        Page<HospitalRoomEntity> hospitalRoomPage = hospitalRoomRepository.findAll(pageable);

        // Chuyển đổi danh sách HospitalRoom entity sang HostpitalRoomResponse DTO
        List<HostpitalRoomResponse> hospitalRoomList = hospitalRoomPage.getContent().stream()
                .map(hospitalMapper::toResponse) // Gọi hàm ánh xạ từ entity sang DTO
                .collect(Collectors.toList());

        // Tạo PageResponse và trả về
        return PageResponse.<HostpitalRoomResponse>builder()
                .currentPage(page)
                .pageSize(hospitalRoomPage.getSize())
                .totalPages(hospitalRoomPage.getTotalPages())
                .totalElements(hospitalRoomPage.getTotalElements())
                .data(hospitalRoomList)
                .build();
    }


    @Override
    public HostpitalRoomResponse getById(String id) {
        return null;
    }

    @Override
    public String delete(String id) {
        return "";
    }
}
