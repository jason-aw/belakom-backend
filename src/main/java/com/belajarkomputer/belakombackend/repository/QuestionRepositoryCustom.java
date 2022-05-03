package com.belajarkomputer.belakombackend.repository;

import com.belajarkomputer.belakombackend.model.entity.Question;
import java.util.List;

public interface QuestionRepositoryCustom {
  List<Question> findAllQuestionByQuizId(String id);
  Question findQuestionByQuizIdAndQuestionId(String quizId, String questionId);
}
