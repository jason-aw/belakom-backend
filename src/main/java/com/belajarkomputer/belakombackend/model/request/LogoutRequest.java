package com.belajarkomputer.belakombackend.model.request;

import lombok.Data;

@Data
public class LogoutRequest {
  private String accessToken;
  private String refreshToken;
}
