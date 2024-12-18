package com.roomfinder.marketing.services;

import com.roomfinder.marketing.controllers.dto.GenericResponseAI;


public interface TrainingService {
    GenericResponseAI getRoomIntents();
    GenericResponseAI getRoomIntentsWithAddress();
    GenericResponseAI getRoomIntentsStatus();
    GenericResponseAI getRoomIntentsTotalArea();
    GenericResponseAI getRoomIntentsInfoUser();
    GenericResponseAI getRoomIntentsUtility();
    GenericResponseAI getRoomIntentsPricingDetails();

}
