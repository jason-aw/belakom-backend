package com.belajarkomputer.belakombackend.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateChapterRequest {
  private String chapterName;
  private int order;
  private String topicId;
  private String description;
  private boolean enableQuiz;
}
