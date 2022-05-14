package com.belajarkomputer.belakombackend.model.response;

import com.belajarkomputer.belakombackend.model.vo.TopicVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopicResponse {
  private List<TopicVo> topicData;
  private boolean success;
  private String error;
}
