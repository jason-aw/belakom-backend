package com.belajarkomputer.belakombackend.repository;

import com.belajarkomputer.belakombackend.model.entity.Chapter;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterRepository extends MongoRepository<Chapter, String>, ChapterRepositoryCustom {
  boolean existsByOrder(int order);
  Chapter findChapterByOrder(int order);
}
