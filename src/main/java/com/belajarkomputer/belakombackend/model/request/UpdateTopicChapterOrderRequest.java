package com.belajarkomputer.belakombackend.model.request;

import lombok.Data;

import java.util.List;

@Data
public class UpdateTopicChapterOrderRequest {
  private String topicId;
  private List<String> chapterOrder;
}
