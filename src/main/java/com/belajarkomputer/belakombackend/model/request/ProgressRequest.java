package com.belajarkomputer.belakombackend.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgressRequest {
  private String chapterId;
  private boolean articleCompleted;
  private Boolean quizCompleted;
  private Integer correct;
}
