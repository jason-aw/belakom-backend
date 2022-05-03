package com.belajarkomputer.belakombackend.service;

import com.belajarkomputer.belakombackend.model.entity.Quiz;
import com.belajarkomputer.belakombackend.model.request.CreateQuizRequest;
import com.belajarkomputer.belakombackend.model.request.UpdateQuizRequest;

import java.util.List;

public interface QuizService {

  List<Quiz> getAllQuizByTopicId(String topicId);
  Quiz getQuizById(String quizId);
  Quiz createQuiz(CreateQuizRequest request);
  void deleteQuiz(String id);
  Quiz updateQuiz(UpdateQuizRequest request);

}
