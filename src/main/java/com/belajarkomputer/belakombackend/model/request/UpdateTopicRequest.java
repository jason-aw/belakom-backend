package com.belajarkomputer.belakombackend.model.request;

import lombok.Data;

@Data
public class UpdateTopicRequest {
  private String id;
  private String topicName;
  private String description;
}
