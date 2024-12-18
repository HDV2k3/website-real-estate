package com.roomfinder.marketing.services;

import com.roomfinder.marketing.controllers.dto.PageResponse;
import com.roomfinder.marketing.controllers.dto.request.HospitalRoomRequest;
import com.roomfinder.marketing.controllers.dto.response.HostpitalRoomResponse;

public interface HospitalRoomService {

    HostpitalRoomResponse createHospitalRoom(HospitalRoomRequest request);

    PageResponse<HostpitalRoomResponse> getAllHospitalRoom(int page,int size);

    HostpitalRoomResponse getById(String id);

    String delete(String id);

}
