package com.roomfinder.marketing.repositories;

import com.roomfinder.marketing.repositories.entities.FeaturedRoomEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
@Repository
public interface FeaturedRepository extends MongoRepository<FeaturedRoomEntity,String> {
    Optional<FeaturedRoomEntity> findFirstByOrderByIndexDesc();
    Optional<FeaturedRoomEntity> findByRoomId(String roomId);
    List<FeaturedRoomEntity> findByExpiryLessThan(Instant time);
    List<FeaturedRoomEntity> findByUserId(int userId);

}
