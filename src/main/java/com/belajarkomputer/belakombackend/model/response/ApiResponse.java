package com.belajarkomputer.belakombackend.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse {
  private boolean success;
  private String message;
}
