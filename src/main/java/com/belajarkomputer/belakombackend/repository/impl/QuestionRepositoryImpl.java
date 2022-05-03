package com.belajarkomputer.belakombackend.repository.impl;

import com.belajarkomputer.belakombackend.model.entity.Question;
import com.belajarkomputer.belakombackend.model.entity.Quiz;
import com.belajarkomputer.belakombackend.repository.QuestionRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class QuestionRepositoryImpl implements QuestionRepositoryCustom {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  public List<Question> findAllQuestionByQuizId(String id) {
    Query query = new Query(Criteria.where("quizId").is(id));

    return mongoTemplate.find(query, Question.class, Question.COLLECTION_NAME);
  }


  @Override
  public Question findQuestionByQuizIdAndQuestionId(String quizId, String questionId) {
    Query query = new Query(Criteria.where("questionId").is(questionId).and("quizId").is(quizId));

    return mongoTemplate.find(query, Question.class, Question.COLLECTION_NAME).get(0);
  }

}
