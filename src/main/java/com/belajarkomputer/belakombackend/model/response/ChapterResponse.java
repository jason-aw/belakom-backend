package com.belajarkomputer.belakombackend.model.response;

import com.belajarkomputer.belakombackend.model.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChapterResponse {
  private boolean success;
  private String id;
  private String chapterName;
  private String description;
  private String htmlContent;
  private String topicId;
  private List<String> imageAttachments = new ArrayList<>();
  private List<Question> questions = new ArrayList<>();
  private boolean enableQuiz;
}
