package com.belajarkomputer.belakombackend.model.request;

import lombok.Data;

@Data
public class UpdateUserRequest {
  private String lastSeenChapterId;
  private String currentlyLearningTopic;
}
