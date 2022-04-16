package com.belajarkomputer.belakombackend.service;

import com.belajarkomputer.belakombackend.model.entity.Topic;
import com.belajarkomputer.belakombackend.model.request.CreateTopicRequest;
import com.belajarkomputer.belakombackend.model.request.UpdateTopicRequest;

import java.util.List;

public interface TopicService {
  Topic createTopic(CreateTopicRequest request);
  List<Topic> getAllTopic();
  void deleteTopic(String id);
  Topic updateTopic(UpdateTopicRequest request);
}
