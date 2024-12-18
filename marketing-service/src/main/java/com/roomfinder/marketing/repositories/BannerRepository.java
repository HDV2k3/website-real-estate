package com.roomfinder.marketing.repositories;

import com.roomfinder.marketing.repositories.entities.BannerEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerRepository  extends MongoRepository<BannerEntity,String> {
}
