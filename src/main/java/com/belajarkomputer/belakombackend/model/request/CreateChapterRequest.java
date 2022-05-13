package com.belajarkomputer.belakombackend.model.request;

import lombok.Data;

@Data
public class CreateChapterRequest {
  private String chapterName;
  private String topicId;
  private String description;
  private boolean enableQuiz;
}
