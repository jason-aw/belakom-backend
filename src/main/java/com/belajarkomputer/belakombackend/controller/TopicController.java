package com.belajarkomputer.belakombackend.controller;

import com.belajarkomputer.belakombackend.exceptions.BadRequestException;
import com.belajarkomputer.belakombackend.model.entity.Topic;
import com.belajarkomputer.belakombackend.model.request.CreateTopicRequest;
import com.belajarkomputer.belakombackend.model.request.UpdateTopicChapterOrderRequest;
import com.belajarkomputer.belakombackend.model.request.UpdateTopicRequest;
import com.belajarkomputer.belakombackend.model.response.ApiResponse;
import com.belajarkomputer.belakombackend.model.vo.TopicVo;
import com.belajarkomputer.belakombackend.security.UserPrincipal;
import com.belajarkomputer.belakombackend.service.TopicService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/api/topic")
@AllArgsConstructor
@Slf4j
public class TopicController {

  private final TopicService topicService;

  @GetMapping("/all")
  public ResponseEntity<?> getAllTopic(@AuthenticationPrincipal UserPrincipal userPrincipal) {
    log.info("user principal: {}", userPrincipal);
    String userId = "";
    if (!Objects.isNull(userPrincipal)) {
      userId = userPrincipal.getId();
    }
    List<TopicVo> result = topicService.getAllTopic(userId);
    return ResponseEntity.ok(ApiResponse.builder().success(false)
        .value(result).build());
  }

  @GetMapping("/{topicName}")
  public ResponseEntity<?> getTopicById(@PathVariable String topicName) {
    try {
      Topic result = topicService.getTopicByTopicName(topicName);
      return ResponseEntity.ok(ApiResponse.builder().success(true)
          .value(result).build());
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ApiResponse.builder().success(false)
          .message(ex.getMessage()).build());
    }
  }

  @PostMapping("/create")
  public ResponseEntity<?> createTopic(@RequestBody CreateTopicRequest request) {
    try {
      Topic result = topicService.createTopic(request);

      return ResponseEntity.ok(ApiResponse.builder().success(true)
          .message("topic " + result.getTopicName() + " berhasil dibuat!").build());

    } catch (BadRequestException ex) {
      return ResponseEntity.badRequest().body(ApiResponse.builder().success(false)
          .message(ex.getMessage()).build());
    }

  }

  @PostMapping("/updateChapterOrder")
  public ResponseEntity<?> updateTopicsChapterOrder(@RequestBody
      UpdateTopicChapterOrderRequest request) {
    try {
      Topic result =
          topicService.updateChapterList(request.getTopicId(), request.getChapterOrder());
      return ResponseEntity.ok(ApiResponse.builder().success(true)
          .message(
              "Chapters Order in Topic with id " + result.getId() + "'s order successfully updated"
                  + result.getChapterOrder()).build());

    } catch (BadRequestException ex) {
      return ResponseEntity.badRequest().body(ApiResponse.builder().success(false)
          .message(ex.getMessage()).build());
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> deleteTopic(@PathVariable String id) {
    try {
      topicService.deleteTopic(id);
      return ResponseEntity.ok(ApiResponse.builder().success(true)
          .message("Topic successfully deleted").build());
    } catch (BadRequestException ex) {
      return ResponseEntity.badRequest().body(ApiResponse.builder().success(false)
          .message(ex.getMessage()).build());
    }
  }

  @PutMapping("/update")
  public ResponseEntity<?> updateTopic(@RequestBody UpdateTopicRequest request) {
    try {
      Topic result = topicService.updateTopic(request);
      return ResponseEntity.ok(ApiResponse.builder().success(true)
          .message("Topic with id " + result.getId() + " successfully updated").build());
    } catch (BadRequestException ex) {
      return ResponseEntity.badRequest().body(ApiResponse.builder().success(false)
          .message(ex.getMessage()).build());
    }
  }

}
