package com.belajarkomputer.belakombackend.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateQuizRequest {
  String id;
  String quizName;
  int order;
  String topicId;
}
