package com.belajarkomputer.belakombackend.controller;

import com.belajarkomputer.belakombackend.exceptions.BadRequestException;
import com.belajarkomputer.belakombackend.model.entity.Topic;
import com.belajarkomputer.belakombackend.model.request.CreateTopicRequest;
import com.belajarkomputer.belakombackend.model.request.UpdateTopicRequest;
import com.belajarkomputer.belakombackend.model.response.ApiResponse;
import com.belajarkomputer.belakombackend.repository.TopicRepository;
import com.belajarkomputer.belakombackend.service.TopicService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

  @GetMapping("/all") public ResponseEntity<?> getAllTopic() {
    try {
      List<Topic> result = topicService.getAllTopic();
      return ResponseEntity.ok(result);
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(new ApiResponse(false, ex.getMessage()));
    }

  }

  @PostMapping("/create")
  public ResponseEntity<?> createTopic(@RequestBody CreateTopicRequest request) {
    try {
      Topic result = topicService.createTopic(request);

      return new ResponseEntity(
          new ApiResponse(true, "topic " + result.getTopicName() + " berhasil dibuat!"),
          HttpStatus.CREATED);

    } catch (BadRequestException ex) {
      return ResponseEntity.badRequest().body(new ApiResponse(false, ex.getMessage()));
    }

  }

  @DeleteMapping("/delete") public ResponseEntity<?> deleteTopic(@RequestParam String id) {
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
