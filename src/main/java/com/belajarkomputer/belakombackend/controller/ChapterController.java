package com.belajarkomputer.belakombackend.controller;


import com.belajarkomputer.belakombackend.exceptions.BadRequestException;
import com.belajarkomputer.belakombackend.model.entity.Chapter;
import com.belajarkomputer.belakombackend.model.request.CreateChapterRequest;
import com.belajarkomputer.belakombackend.model.request.UpdateChapterRequest;
import com.belajarkomputer.belakombackend.model.response.ApiResponse;
import com.belajarkomputer.belakombackend.model.vo.ChapterVo;
import com.belajarkomputer.belakombackend.security.UserPrincipal;
import com.belajarkomputer.belakombackend.service.ChapterService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/api/chapter")
@AllArgsConstructor
@Slf4j
public class ChapterController {

  private ChapterService chapterService;

  @GetMapping("/getAllByTopicId")
  public ResponseEntity<?> getAllChapterByTopicId(@RequestParam String topicId,
      @AuthenticationPrincipal UserPrincipal userPrincipal) {
    String userId = "";
    if (!Objects.isNull(userPrincipal)) {
      userId = userPrincipal.getId();
    }
    List<ChapterVo> result = chapterService.getAllChaptersByTopicIdAndUserId(topicId, userId);
    return ResponseEntity.ok(ApiResponse.builder().success(true).value(result).build());
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getChapterById(@PathVariable String id) {
    try {
      Chapter chapter = this.chapterService.findChapterById(id);
      return ResponseEntity.ok(ApiResponse.builder()
          .success(true).value(chapter).build());
    } catch (BadRequestException ex) {
      log.error("Chapter not found");
      return ResponseEntity.badRequest().body(ApiResponse.builder()
          .success(false).message(ex.getMessage()).build());
    }
  }

  @PostMapping("/create")
  public ResponseEntity<?> createChapter(@RequestBody CreateChapterRequest request) {
    try {
      this.chapterService.createChapter(request);
      return ResponseEntity.ok(ApiResponse.builder().success(true).build());
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ApiResponse.builder()
          .success(false).message(ex.getMessage()).build());
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> deleteChapter(@PathVariable String id) {
    try {
      this.chapterService.deleteChapter(id);
      return ResponseEntity.ok(ApiResponse.builder().success(true).build());
    } catch (BadRequestException ex) {
      return ResponseEntity.badRequest().body(ApiResponse.builder()
          .success(false).message(ex.getMessage()).build());
    }
  }

  @Secured("ROLE_ADMIN")
  @PutMapping("/update")
  public ResponseEntity<?> updateChapter(@RequestBody UpdateChapterRequest request) {
    try {
      Chapter chapter = this.chapterService.updateChapter(request);
      return ResponseEntity.ok(ApiResponse.builder().success(true)
          .value(chapter).build());
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ApiResponse.builder()
          .success(false).message(ex.getMessage()).build());
    }
  }

}
