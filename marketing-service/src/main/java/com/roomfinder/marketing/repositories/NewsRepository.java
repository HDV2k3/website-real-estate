package com.roomfinder.marketing.repositories;

import com.roomfinder.marketing.repositories.entities.NewsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends MongoRepository<NewsEntity, String> {
}
