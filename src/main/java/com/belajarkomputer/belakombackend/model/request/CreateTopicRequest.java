package com.belajarkomputer.belakombackend.model.request;

import lombok.Data;

@Data
public class CreateTopicRequest {
  private String topicName;
  private String description;
}
