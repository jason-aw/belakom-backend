package com.belajarkomputer.belakombackend.repository;

import com.belajarkomputer.belakombackend.model.entity.Question;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends MongoRepository<Question, String>, QuestionRepositoryCustom {
  boolean existsByOrder(int order);
}
