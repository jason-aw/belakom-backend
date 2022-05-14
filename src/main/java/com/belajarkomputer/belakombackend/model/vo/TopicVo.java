package com.belajarkomputer.belakombackend.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicVo {
  private String id;
  private String topicName;
  private String description;
  private List<String> chapterOrder;
  private double topicCompletion;
}
