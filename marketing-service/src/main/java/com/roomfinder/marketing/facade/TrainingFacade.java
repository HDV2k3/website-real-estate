package com.roomfinder.marketing.facade;

import com.roomfinder.marketing.controllers.dto.GenericResponseAI;
import com.roomfinder.marketing.services.TrainingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TrainingFacade {
    TrainingService trainingService;
    public GenericResponseAI getRoomIntents()
    {
        return trainingService.getRoomIntents();
    }

    public GenericResponseAI getRoomIntentsWithAddress()
    {
        return trainingService.getRoomIntentsWithAddress();
    }
    public GenericResponseAI getRoomIntentsStatus()
    {
        return trainingService.getRoomIntentsStatus();
    }
    public GenericResponseAI getRoomIntentsTotalArea()
    {
        return trainingService.getRoomIntentsTotalArea();
    }

    public GenericResponseAI getRoomIntentsInfoUser()
    {
        return trainingService.getRoomIntentsInfoUser();
    }
    public GenericResponseAI getRoomIntentsUtility()
    {
        return trainingService.getRoomIntentsUtility();
    }
    public GenericResponseAI getRoomIntentsPricingDetails()
    {
        return trainingService.getRoomIntentsPricingDetails();
    }

}
