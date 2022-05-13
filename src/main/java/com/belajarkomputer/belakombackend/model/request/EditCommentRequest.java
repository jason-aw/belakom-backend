package com.belajarkomputer.belakombackend.model.request;

import lombok.Data;

@Data
public class EditCommentRequest {
  private String commentId;
  private String content;
}
