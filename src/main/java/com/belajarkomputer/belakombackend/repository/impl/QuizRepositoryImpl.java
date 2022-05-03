package com.belajarkomputer.belakombackend.repository.impl;

import com.belajarkomputer.belakombackend.model.entity.Quiz;
import com.belajarkomputer.belakombackend.repository.QuizRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class QuizRepositoryImpl implements QuizRepositoryCustom {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  public List<Quiz> findAllQuizByTopicId(String id) {
    Query query = new Query(Criteria.where("topicId").is(id));
    query.with(Sort.by(Sort.Direction.ASC, "order"));

    return mongoTemplate.find(query, Quiz.class, Quiz.COLLECTION_NAME);
  }

}
