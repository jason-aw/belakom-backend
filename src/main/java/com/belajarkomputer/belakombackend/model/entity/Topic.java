package com.belajarkomputer.belakombackend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = Topic.COLLECTION_NAME)
public class Topic {
  public static final String COLLECTION_NAME = "topics";
  @Id
  private String id;
  @Indexed(unique = true)
  private String topicName;
  private String description;
  private List<String> chapterOrder;
}
