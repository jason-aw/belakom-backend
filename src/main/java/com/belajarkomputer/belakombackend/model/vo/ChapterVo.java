package com.belajarkomputer.belakombackend.model.vo;

import com.belajarkomputer.belakombackend.model.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChapterVo {
  private String id;
  private String topicId;
  private boolean enableQuiz;
  private String chapterName;
  private String description;
  private String htmlContent;
  private List<String> imageAttachments =  new ArrayList<>();
  private List<Question> questions = new ArrayList<>();
  private double chapterCompletion;
}
