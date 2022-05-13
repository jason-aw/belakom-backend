package com.belajarkomputer.belakombackend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = Comment.COLLECTION_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {
  public static final String COLLECTION_NAME = "comments";
  @Id
  private String id;
  private String userId;
  private String chapterId;
  private String content;
  private Date lastUpdated;
  private String parentCommentId;
}
