package com.belajarkomputer.belakombackend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = Quiz.COLLECTION_NAME)
public class Quiz {
  public static final String COLLECTION_NAME = "quizzes";
  @Id
  String id;
  String quizName;
  int order;
  String topicId;
}
