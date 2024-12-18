package com.roomfinder.marketing.repositories;

import com.roomfinder.marketing.repositories.entities.HospitalRoomEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRoomRepository extends MongoRepository<HospitalRoomEntity,String> {

}
