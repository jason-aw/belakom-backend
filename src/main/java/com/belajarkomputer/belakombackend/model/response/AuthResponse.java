package com.belajarkomputer.belakombackend.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
  private String accessToken;
  private String refreshToken;
  private List<String> roles;
  private String email;
  private boolean success;
  private String error;
  private String userId;

  private String name;
  private String imageUrl;

  private List<String> lastSeenChapters = new ArrayList<>();
  private String currentlyLearningTopic;
}
