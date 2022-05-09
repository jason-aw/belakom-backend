package com.belajarkomputer.belakombackend.controller;


import com.belajarkomputer.belakombackend.exceptions.BadRequestException;
import com.belajarkomputer.belakombackend.model.entity.Chapter;
import com.belajarkomputer.belakombackend.model.request.CreateChapterRequest;
import com.belajarkomputer.belakombackend.model.request.UpdateChapterRequest;
import com.belajarkomputer.belakombackend.model.response.ApiResponse;
import com.belajarkomputer.belakombackend.model.response.ChapterResponse;
import com.belajarkomputer.belakombackend.service.ChapterService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

@RestController
@RequestMapping(value = "/api/chapter")
@AllArgsConstructor
@Slf4j
public class ChapterController {

  private ChapterService chapterService;

  @GetMapping("/getAllByTopicId")
  public ResponseEntity<?> getAllChapterByTopicId(@RequestParam String id) {
    try {
      List<Chapter> result = chapterService.getAllChaptersByTopicId(id);
      return ResponseEntity.ok(result);
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(new ApiResponse(false, ex.getMessage()));
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<ChapterResponse> getChapterById(@PathVariable String id) {
    try {
      Chapter chapter = this.chapterService.findChapterById(id);
      return ResponseEntity.ok(ChapterResponse.builder()
          .success(true)
          .id(chapter.getId())
          .chapterName(chapter.getChapterName())
          .description(chapter.getDescription())
          .htmlContent(chapter.getHtmlContent())
          .topicId(chapter.getTopicId())
          .enableQuiz(chapter.isEnableQuiz())
          .imageAttachments(chapter.getImageAttachments())
              .questions(chapter.getQuestions())
          .build());
    } catch (BadRequestException ex) {
      log.error("Chapter not found");
      return ResponseEntity.badRequest().body(ChapterResponse.builder().success(false).build());
    }
  }

  @PostMapping("/create")
  public ResponseEntity<?> createChapter(@RequestBody CreateChapterRequest request) {
    try {
      Chapter result = chapterService.createChapter(request);
      return ResponseEntity.ok(ChapterResponse
          .builder().success(true).build());
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(new ApiResponse(false, ex.getMessage()));
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> deleteChapter(@PathVariable String id) {
    try {
      chapterService.deleteChapter(id);
      return ResponseEntity.ok(new ApiResponse(true, "Chapter successfully deleted"));
    } catch (BadRequestException ex) {
      return ResponseEntity.badRequest().body(new ApiResponse(false, ex.getMessage()));
    }
  }

  @PutMapping("/update")
  public ResponseEntity<?> updateChapter(@RequestBody UpdateChapterRequest request) {
    try {
      Chapter result = chapterService.updateChapter(request);
      return ResponseEntity.ok(
          new ApiResponse(true, "Chapter with id " + result.getId() + " successfully updated"));
    } catch (BadRequestException ex) {
      return ResponseEntity.badRequest().body(new ApiResponse(false, ex.getMessage()));
    }
  }

}
