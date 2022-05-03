package com.belajarkomputer.belakombackend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = Question.COLLECTION_NAME)
public class Question {
  public static final String COLLECTION_NAME = "questions";
  @Id
  String id;
  String quizId;
  String topicId;
  String type;


  String question;
  String correctAnswer;
  int order;

  //for MPC question
  List<String> fakeAnswer;
}
