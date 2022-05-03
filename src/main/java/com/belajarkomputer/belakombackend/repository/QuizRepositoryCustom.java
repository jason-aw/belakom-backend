package com.belajarkomputer.belakombackend.repository;

import com.belajarkomputer.belakombackend.model.entity.Quiz;

import java.util.List;

public interface QuizRepositoryCustom {

  List<Quiz> findAllQuizByTopicId(String id);
}
