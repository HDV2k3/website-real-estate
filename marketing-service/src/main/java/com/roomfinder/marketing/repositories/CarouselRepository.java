package com.roomfinder.marketing.repositories;

import com.roomfinder.marketing.repositories.entities.CarouselEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarouselRepository extends MongoRepository<CarouselEntity,String> {
}
