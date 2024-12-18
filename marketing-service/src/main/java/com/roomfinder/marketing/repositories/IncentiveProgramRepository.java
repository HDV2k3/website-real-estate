package com.roomfinder.marketing.repositories;

import com.roomfinder.marketing.repositories.entities.IncentiveProgramEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IncentiveProgramRepository extends MongoRepository<IncentiveProgramEntity, String> {


    Optional<IncentiveProgramEntity> findIncentiveProgramEntityByStatus(String status);

}
