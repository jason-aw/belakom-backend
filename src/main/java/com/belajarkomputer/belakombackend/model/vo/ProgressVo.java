package com.belajarkomputer.belakombackend.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgressVo {
  private String userId;
  private String topicId;
  private double topicCompletion;

  private String chapterId;
  private boolean articleCompleted;
  private boolean quizCompleted;
  private int correct;
  private int totalQuestions;
}
