package com.belajarkomputer.belakombackend.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
  private String accessToken;
  private String refreshToken;
  private String tokenType;
  private boolean success;
  private String error;

  public AuthResponse(String accessToken, String refreshToken) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.success = true;
    this.tokenType = "Bearer";
  }
}
