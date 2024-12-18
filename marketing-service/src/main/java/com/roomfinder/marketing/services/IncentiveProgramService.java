package com.roomfinder.marketing.services;

import com.roomfinder.marketing.controllers.dto.request.IncentiveProgramRequest;
import com.roomfinder.marketing.controllers.dto.response.IncentiveProgramResponse;

import java.util.List;

public interface IncentiveProgramService {

    IncentiveProgramResponse createInventiveProgram(IncentiveProgramRequest request);

    IncentiveProgramResponse updateInventiveProgram(String id, IncentiveProgramRequest request);

    IncentiveProgramResponse getInventiveProgram(String id);
    IncentiveProgramResponse getInventiveProgramByStatus(String status);
    void deleteInventiveProgram(String id);

    List<IncentiveProgramResponse> getAllInventivePrograms();
}
