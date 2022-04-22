package com.belajarkomputer.belakombackend.controller;

import com.belajarkomputer.belakombackend.exceptions.BadRequestException;
import com.belajarkomputer.belakombackend.model.entity.Topic;
import com.belajarkomputer.belakombackend.model.request.CreateTopicRequest;
import com.belajarkomputer.belakombackend.model.request.UpdateTopicRequest;
import com.belajarkomputer.belakombackend.model.response.ApiResponse;
import com.belajarkomputer.belakombackend.service.TopicService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/topic")
@AllArgsConstructor
@Slf4j
public class TopicController {
  
  private final TopicService topicService;

  @GetMapping("/all")
  public ResponseEntity<?> getAllTopic() {
    try {
      List<Topic> result = topicService.getAllTopic();
      return ResponseEntity.ok(result);
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(new ApiResponse(false, ex.getMessage()));
    }
  }

  @GetMapping("/{topicName}")
  public ResponseEntity<?> getTopicById(@PathVariable String topicName) {
    try {
      Topic result = topicService.getTopicByTopicName(topicName);
      return ResponseEntity.ok(result);
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(new ApiResponse(false, ex.getMessage()));
    }
  }

  @PostMapping("/create")
  public ResponseEntity<?> createTopic(@RequestBody CreateTopicRequest request) {
    try {
      Topic result = topicService.createTopic(request);

      return ResponseEntity.status(HttpStatus.CREATED).body(
          new ApiResponse(true, "topic " + result.getTopicName() + " berhasil dibuat!"));

    } catch (BadRequestException ex) {
      return ResponseEntity.badRequest().body(new ApiResponse(false, ex.getMessage()));
    }

  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> deleteTopic(@PathVariable String id) {
    try {
      topicService.deleteTopic(id);
      return ResponseEntity.ok(new ApiResponse(true, "Topic successfully deleted"));
    } catch (BadRequestException ex) {
      return ResponseEntity.badRequest().body(new ApiResponse(false, ex.getMessage()));
    }
  }

  @PostMapping("/update")
  public ResponseEntity<?> updateTopic(@RequestBody UpdateTopicRequest request) {
    try {
      Topic result = topicService.updateTopic(request);
      return ResponseEntity.ok(
          new ApiResponse(true, "Topic with id " + result.getId() + " successfully updated"));
    } catch (BadRequestException ex) {
      return ResponseEntity.badRequest().body(new ApiResponse(false, ex.getMessage()));
    }
  }

}
