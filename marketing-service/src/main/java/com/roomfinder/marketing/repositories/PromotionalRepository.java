package com.roomfinder.marketing.repositories;

import com.roomfinder.marketing.repositories.entities.PromotionalRoomEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionalRepository extends MongoRepository<PromotionalRoomEntity, String> {
    Optional<PromotionalRoomEntity> findByRoomId(String roomId);
    List<PromotionalRoomEntity> findAllByRoomId(String roomId);
    List<PromotionalRoomEntity> findAllByFixPriceNotNull();
    List<PromotionalRoomEntity> findAllByRoomIdIn(List<String> roomIds);

}

