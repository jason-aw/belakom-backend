package com.belajarkomputer.belakombackend.repository;

import com.belajarkomputer.belakombackend.model.entity.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TopicRepository extends MongoRepository<Topic, String> {
    List<Topic> getAllTopics();
    Topic findTopicById(String Id);
    boolean existsByTopicName(String topicName);
    boolean existsById(String id);

}
