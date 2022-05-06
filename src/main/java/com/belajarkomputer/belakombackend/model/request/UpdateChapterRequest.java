package com.belajarkomputer.belakombackend.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateChapterRequest {
  private String id;
  private String chapterName;
  private String description;
  private boolean enableQuiz;
  private String htmlContent;
}
