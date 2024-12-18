package com.roomfinder.marketing.facade;

import com.roomfinder.marketing.controllers.dto.request.IncentiveProgramRequest;
import com.roomfinder.marketing.controllers.dto.response.IncentiveProgramResponse;
import com.roomfinder.marketing.services.IncentiveProgramService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IncentiveProgramFacade {
    IncentiveProgramService incentiveProgramService;

    // This class should have the following methods:
    // - createIncentiveProgram
    // - updateIncentiveProgram
    // - deleteIncentiveProgram
    // - getIncentiveProgram
    // - getIncentivePrograms
    // - getIncentiveProgramsByStatus

    public IncentiveProgramResponse createIncentiveProgram(IncentiveProgramRequest request) {
        return incentiveProgramService.createInventiveProgram(request);
    }

    public IncentiveProgramResponse updateIncentiveProgram(String id, IncentiveProgramRequest request) {
        return incentiveProgramService.updateInventiveProgram(id, request);
    }

    public IncentiveProgramResponse getIncentiveProgram(String id) {
        return incentiveProgramService.getInventiveProgram(id);
    }

    public IncentiveProgramResponse getIncentiveProgramByStatus(String status) {
        return incentiveProgramService.getInventiveProgramByStatus(status);
    }

    public String deleteIncentiveProgram(String id) {
        incentiveProgramService.deleteInventiveProgram(id);
        return "Incentive Program deleted successfully";
    }

    public List<IncentiveProgramResponse> getAllIncentivePrograms() {
        return incentiveProgramService.getAllInventivePrograms();
    }
}
