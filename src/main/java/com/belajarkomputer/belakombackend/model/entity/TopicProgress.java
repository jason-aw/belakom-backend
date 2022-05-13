package com.belajarkomputer.belakombackend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = TopicProgress.COLLECTION_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopicProgress {
  public static final String COLLECTION_NAME = "topic_progress";
  @Id
  private String id;
  private String topicId;
  private String userId;
  private double topicCompletion;

  /*
  * topicCompletion:
  * every chapter has equal points, if there is a quiz, articleCompleted 50%, quizCompleted 50%
  * */
}
