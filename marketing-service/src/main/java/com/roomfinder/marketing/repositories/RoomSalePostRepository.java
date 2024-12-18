package com.roomfinder.marketing.repositories;

import com.roomfinder.marketing.repositories.entities.RoomSalePostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface RoomSalePostRepository extends MongoRepository<RoomSalePostEntity, String> {


    List<RoomSalePostEntity> findAllByRoomId(String roomId);
    Optional<RoomSalePostEntity> findByRoomId(String roomId);
    List<RoomSalePostEntity> findAllByRoomId(String roomId, Sort sort);

    @Query("{ $text: { $search: ?0 } }")
    Page<RoomSalePostEntity> searchByText(String searchTerm, Pageable pageable);

    Page<RoomSalePostEntity> findAllByUserId(int userId, Pageable pageable);

    Page<RoomSalePostEntity> findAllByUserIdAndStatus(int userId, String status, Pageable pageable);

    Page<RoomSalePostEntity> findAllByStatusNotIn(List<String> excludedStatuses, Pageable pageable);

    @Query("{ 'status': { $nin: ?0 } }")
    Page<RoomSalePostEntity> findByStatusNotInOrderByIndexAscCreatedDateDesc(
            List<String> excludedStatuses,
            Pageable pageable
    );
}
