package com.belajarkomputer.belakombackend.repository;

import com.belajarkomputer.belakombackend.model.entity.Question;
import java.util.List;

public interface QuestionRepositoryCustom {
  List<Question> findAllQuestionByChapterId(String id);
  List<Question> findAllQuestionByTopicId(String id);
  Question findQuestionByChapterIdAndQuestionId(String chapterId, String questionId);
}
