package com.belajarkomputer.belakombackend.repository;

import com.belajarkomputer.belakombackend.model.entity.Topic;

public interface TopicRepositoryCustom {

  Topic findTopicByTopicName(String topicName);

}
