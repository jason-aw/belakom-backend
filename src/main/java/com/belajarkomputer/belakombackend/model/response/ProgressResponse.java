package com.belajarkomputer.belakombackend.model.response;

import com.belajarkomputer.belakombackend.model.entity.ChapterProgress;
import com.belajarkomputer.belakombackend.model.entity.TopicProgress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgressResponse {
  private List<TopicProgress> topicProgressList;
  private List<ChapterProgress> chapterProgressList;
  private boolean success;
  private String error;
}
