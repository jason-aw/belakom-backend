package com.belajarkomputer.belakombackend.repository;

import com.belajarkomputer.belakombackend.model.entity.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TopicRepository extends MongoRepository<Topic, String> {
  boolean existsByTopicName(String topicName);
  Topic findTopicByTopicName(String topicName);
}
