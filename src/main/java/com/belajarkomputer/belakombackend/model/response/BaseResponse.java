package com.belajarkomputer.belakombackend.model.response;

import com.belajarkomputer.belakombackend.model.entity.ErrorInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {
  boolean success;
  ErrorInfo errorInfo;
}
