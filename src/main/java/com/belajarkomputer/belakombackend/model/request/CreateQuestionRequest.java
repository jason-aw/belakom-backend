package com.belajarkomputer.belakombackend.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateQuestionRequest {

  String quizId;
  String topicId;

  String question;
  String correctAnswer;
  int order;
  String Type;

  //for MPC question
  List<String> fakeAnswer;

}
