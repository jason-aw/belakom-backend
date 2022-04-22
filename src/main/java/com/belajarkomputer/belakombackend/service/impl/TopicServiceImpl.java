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
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

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
    if (!this.topicRepository.existsById(request.getId())) {
      throw new BadRequestException("Topic dengan id " + " tidak ada!");
    }

    if (this.topicRepository.existsByTopicName(request.getTopicName())) {
      throw new BadRequestException("Topic dengan nama " + request.getTopicName() + " sudah ada!");
    }

    Topic updateTopic = Topic.builder()
        .id(request.getId())
        .topicName(request.getTopicName())
        .description(request.getDescription())
        .build();

    return topicRepository.save(updateTopic);
  }

  @Override
  public Topic getTopicByTopicName(String topicName) {
    Topic result = topicRepository.findTopicByTopicName(topicName);
    if (ObjectUtils.isEmpty(result)) {
      throw new BadRequestException("Topic dengan nama " + topicName + " tidak ada!");
    }
    return result;
  }
}
