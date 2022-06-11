package com.belajarkomputer.belakombackend.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserChapterHistory {

  private String chapterId;
  private String chapterName;
  private String topicId;
  private String topicName;

}
