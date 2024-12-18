package com.roomfinder.marketing.repositories;

import com.roomfinder.marketing.repositories.entities.FavoriteEntity;
import com.roomfinder.marketing.repositories.entities.FeaturedRoomEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends MongoRepository<FavoriteEntity,String> {
    Optional<FavoriteEntity> findFirstByOrderByIndexDesc();
    Optional<FavoriteEntity> findByRoomId(String roomId);
    Page<FavoriteEntity> findByUserId(int userId, Pageable pageable);

}
