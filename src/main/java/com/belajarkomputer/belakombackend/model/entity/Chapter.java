package com.belajarkomputer.belakombackend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = Chapter.COLLECTION_NAME)
public class Chapter {
  public static final String COLLECTION_NAME = "chapters";
  @Id
  private String id;
  private int order;
  private String topicId;
  private boolean enableQuiz;
  @Indexed(unique = true)
  private String chapterName;
  private String description;
  private String htmlContent;
  private List<String> imageAttachments =  new ArrayList<>();
}
