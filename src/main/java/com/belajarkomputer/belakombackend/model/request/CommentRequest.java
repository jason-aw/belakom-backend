package com.belajarkomputer.belakombackend.model.request;

import lombok.Data;

@Data
public class CommentRequest {
  private String chapterId;
  private String content;
  private String parentCommentId;
}
