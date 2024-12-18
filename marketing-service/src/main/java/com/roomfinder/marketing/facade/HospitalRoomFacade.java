package com.roomfinder.marketing.facade;

import com.roomfinder.marketing.controllers.dto.PageResponse;
import com.roomfinder.marketing.controllers.dto.request.HospitalRoomRequest;
import com.roomfinder.marketing.controllers.dto.response.HostpitalRoomResponse;
import com.roomfinder.marketing.services.HospitalRoomService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HospitalRoomFacade {
    HospitalRoomService hospitalRoomService;

    public HostpitalRoomResponse createHospitalRoom(HospitalRoomRequest request)
    {
        return  hospitalRoomService.createHospitalRoom(request);
    }


    public PageResponse<HostpitalRoomResponse> getAllHospitalRoom(int page,int size)
    {
        return hospitalRoomService.getAllHospitalRoom(page, size);
    }
}
