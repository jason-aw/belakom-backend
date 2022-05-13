package com.belajarkomputer.belakombackend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = ChapterProgress.COLLECTION_NAME)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChapterProgress {
  public static final String COLLECTION_NAME = "chapter_progress";
  @Id
  private String id;
  private String userId;
  private String chapterId;
  private String topicId;
  private boolean articleCompleted;
  private boolean quizCompleted;
  private int correct;
  private int totalQuestions;

  /*
  * article content updated => articleCompleted = false
  * chapter has no quiz => quizCompleted, correct, totalQuestions = null
  * chapter has quiz => quizCompleted = false; correct = 0; totalQuestions get from chapter
  *
  * chapter quiz updated
  * quiz deleted => don't delete history
  * quiz created => quizCompleted = false, reset correct
  * */

}
