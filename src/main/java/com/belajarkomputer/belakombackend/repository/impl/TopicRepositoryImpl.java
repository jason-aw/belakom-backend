package com.belajarkomputer.belakombackend.repository.impl;

import com.belajarkomputer.belakombackend.model.entity.Topic;
import com.belajarkomputer.belakombackend.repository.TopicRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class TopicRepositoryImpl implements TopicRepositoryCustom {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  public Topic findTopicByTopicName(String topicName) {
    Query query = new Query(Criteria.where("topicName").is(topicName));

    return mongoTemplate.find(query, Topic.class, Topic.COLLECTION_NAME).get(0);
  }

}
