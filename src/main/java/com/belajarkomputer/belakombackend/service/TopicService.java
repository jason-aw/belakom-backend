package com.belajarkomputer.belakombackend.service;

import com.belajarkomputer.belakombackend.model.entity.Topic;
import com.belajarkomputer.belakombackend.model.request.CreateTopicRequest;
import com.belajarkomputer.belakombackend.model.request.UpdateTopicRequest;
import com.belajarkomputer.belakombackend.model.vo.TopicVo;

import java.util.List;

public interface TopicService {
  Topic createTopic(CreateTopicRequest request);
  List<TopicVo> getAllTopic(String userId) throws Exception;
  void deleteTopic(String id);
  Topic updateTopic(UpdateTopicRequest request);
  void addChapterList(String topicId, String chapterId);
  void removeChapterFromOrder(String topicId, String chapterId);
  Topic updateChapterList(String topicId, List<String> chapterOrder);
  Topic getTopicByTopicName(String topicName);
  Topic findTopicById(String id);
}
