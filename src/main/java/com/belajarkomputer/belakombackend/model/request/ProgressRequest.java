package com.belajarkomputer.belakombackend.model.request;

import lombok.Data;

@Data
public class ProgressRequest {
  private String chapterId;
  private boolean articleCompleted;
  private Boolean quizCompleted;
  private Integer correct;
}
