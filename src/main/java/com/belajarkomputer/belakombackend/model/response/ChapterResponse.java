package com.belajarkomputer.belakombackend.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChapterResponse {
  private boolean success;
  private String id;
  private String chapterName;
  private int order;
  private String description;
  private String htmlContent;
  private String topicId;
  private boolean enableQuiz;
}
