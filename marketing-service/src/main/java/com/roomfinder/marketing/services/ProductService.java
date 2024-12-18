package com.roomfinder.marketing.services;

import com.roomfinder.marketing.repositories.datamodel.Room;

public interface ProductService {
    Room getRoomById(String id);
}
