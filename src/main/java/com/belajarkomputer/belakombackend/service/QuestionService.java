package com.belajarkomputer.belakombackend.service;

import com.belajarkomputer.belakombackend.model.entity.Question;
import com.belajarkomputer.belakombackend.model.request.CreateQuestionRequest;
import com.belajarkomputer.belakombackend.model.request.UpdateQuestionRequest;

import java.util.List;

public interface QuestionService {

  List<Question> getAllQuestionByQuizId(String quizId);
  Question getQuestionById(String quizId, String questionId);
  Question createQuestion(CreateQuestionRequest request);
  void deleteQuestion(String id);
  Question updateQuestion(UpdateQuestionRequest request);

}
