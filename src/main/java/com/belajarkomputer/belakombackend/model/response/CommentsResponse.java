package com.belajarkomputer.belakombackend.model.response;

import com.belajarkomputer.belakombackend.model.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentsResponse {
  private boolean success;
  private List<Comment> mainComments;
  private Map<String, List<Comment>> commentRepliesMap = new HashMap<>();
}
