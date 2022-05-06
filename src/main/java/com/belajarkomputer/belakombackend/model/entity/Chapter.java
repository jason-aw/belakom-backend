package com.belajarkomputer.belakombackend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = Chapter.COLLECTION_NAME)
public class Chapter {
  public static final String COLLECTION_NAME = "chapters";
  @Id
  private String id;
  private String topicId;
  private boolean enableQuiz;
  @Indexed(unique = true)
  private String chapterName;
  private String description;
  private String htmlContent;
}
