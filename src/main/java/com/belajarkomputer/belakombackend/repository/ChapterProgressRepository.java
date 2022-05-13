package com.belajarkomputer.belakombackend.repository;

import com.belajarkomputer.belakombackend.model.entity.ChapterProgress;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterProgressRepository extends MongoRepository<ChapterProgress, String> {
  List<ChapterProgress> findChapterProgressesByTopicIdAndUserId(String topicId, String userId);
  Optional<ChapterProgress> findChapterProgressByChapterIdAndUserId(String chapterId, String userId);
}
