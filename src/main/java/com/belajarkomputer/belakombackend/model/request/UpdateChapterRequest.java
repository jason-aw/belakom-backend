package com.belajarkomputer.belakombackend.model.request;

import com.belajarkomputer.belakombackend.model.entity.Question;
import lombok.Data;

import java.util.List;

@Data
public class UpdateChapterRequest {
  private String id;
  private String chapterName;
  private String description;
  private boolean enableQuiz;
  private String htmlContent;
  private List<String> imageAttachments;
  private List<Question> questions;
}
