package com.belajarkomputer.belakombackend.service.impl;

import com.belajarkomputer.belakombackend.exceptions.BadRequestException;
import com.belajarkomputer.belakombackend.model.entity.Topic;
import com.belajarkomputer.belakombackend.model.request.CreateTopicRequest;
import com.belajarkomputer.belakombackend.model.request.UpdateTopicRequest;
import com.belajarkomputer.belakombackend.repository.TopicRepository;
import com.belajarkomputer.belakombackend.service.TopicService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class TopicServiceImpl implements TopicService {

  private TopicRepository topicRepository;

  @Override
  public List<Topic> getAllTopic() {
    return this.topicRepository.findAll();
  }

  @Override
  public Topic createTopic(CreateTopicRequest request) {
    if (this.topicRepository.existsByTopicName(request.getTopicName())) {
      throw new BadRequestException("Topic dengan nama " + request.getTopicName() + " sudah ada!");
    }

    Topic newTopic = Topic.builder()
        .topicName(request.getTopicName())
        .description(request.getDescription())
        .chapterOrder(new ArrayList<String>())
        .build();

    return this.topicRepository.save(newTopic);
  }

  @Override
  public void deleteTopic(String id) {
    if (!this.topicRepository.existsById(id)) {
      throw new BadRequestException("Topic dengan id " + " tidak ada!");
    }

    this.topicRepository.deleteById(id);
  }

  @Override
  public Topic updateTopic(UpdateTopicRequest request) {
    if (this.topicRepository.existsByTopicName(request.getTopicName())) {
      throw new BadRequestException("Topic dengan nama " + request.getTopicName() + " sudah ada!");
    }

    Topic topic = this.findTopicById(request.getId());
    topic.setTopicName(request.getTopicName());
    topic.setDescription(request.getDescription());
    return topicRepository.save(topic);
  }

  @Override
  public void addChapterList(String topicId, String chapterId) {
    Topic topic = this.findTopicById(topicId);
    topic.getChapterOrder().add(chapterId);
    topicRepository.save(topic);
  }

  @Override
  public void removeChapterFromOrder(String topicId, String chapterId) {
    Topic topic = this.findTopicById(topicId);
    topic.getChapterOrder().remove(chapterId);
    topicRepository.save(topic);
  }

  @Override
  public Topic updateChapterList(String topicId, List<String> chapterOrder) {
    Topic topic = this.findTopicById(topicId);
    topic.setChapterOrder(chapterOrder);
    return topicRepository.save(topic);
  }

  @Override
  public Topic getTopicByTopicName(String topicName) {
    Topic result = topicRepository.findTopicByTopicName(topicName);
    if (ObjectUtils.isEmpty(result)) {
      throw new BadRequestException("Topic dengan nama " + topicName + " tidak ada!");
    }
    return result;
  }

  @Override
  public Topic findTopicById(String id) {
    Topic topic = this.topicRepository.findById(id).orElse(null);
    if (Objects.isNull(topic)) {
      throw new BadRequestException("Topic with id " + id + " does not exist");
    }
    return topic;
  }
}
