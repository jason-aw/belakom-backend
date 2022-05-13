package com.belajarkomputer.belakombackend.repository;

import com.belajarkomputer.belakombackend.model.entity.TopicProgress;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicProgressRepository extends MongoRepository<TopicProgress, String> {
  List<TopicProgress> findTopicProgressesByUserId(String userId);
  Optional<TopicProgress> findTopicProgressesByTopicIdAndUserId(String topicId, String userId);
}
