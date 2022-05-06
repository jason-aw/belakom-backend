package com.belajarkomputer.belakombackend.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateChapterRequest {
  private String id;
  private String chapterName;
  private String description;
  private boolean enableQuiz;
  private String htmlContent;
  private List<String> imageAttachments;
}
