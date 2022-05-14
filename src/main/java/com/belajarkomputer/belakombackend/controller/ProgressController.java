package com.belajarkomputer.belakombackend.controller;

import com.belajarkomputer.belakombackend.model.entity.ChapterProgress;
import com.belajarkomputer.belakombackend.model.entity.TopicProgress;
import com.belajarkomputer.belakombackend.model.request.ProgressRequest;
import com.belajarkomputer.belakombackend.model.response.ApiResponse;
import com.belajarkomputer.belakombackend.model.response.ProgressResponse;
import com.belajarkomputer.belakombackend.model.vo.ProgressVo;
import com.belajarkomputer.belakombackend.security.UserPrincipal;
import com.belajarkomputer.belakombackend.service.ProgressService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/api/progress")
@AllArgsConstructor
@Slf4j
public class ProgressController {

  private ProgressService progressService;

  @GetMapping("/topics")
  public ResponseEntity<ProgressResponse> getTopicProgressByUserId(@AuthenticationPrincipal UserPrincipal userPrincipal) {
    try {
      List<TopicProgress> topicProgressList =
          this.progressService.findTopicProgressByUserId(userPrincipal.getId());
      return ResponseEntity.ok(ProgressResponse.builder()
          .success(true)
          .topicProgressList(topicProgressList)
          .build());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ProgressResponse.builder()
          .success(false)
          .error(e.getMessage())
          .build());
    }
  }

  @GetMapping("/chapters/{topicId}")
  public ResponseEntity<ProgressResponse> getChapterProgressByTopicIdAndUserId(@AuthenticationPrincipal UserPrincipal userPrincipal,
      @PathVariable String topicId) {
    try {
      List<ChapterProgress> chapterProgressList =
          this.progressService.findChapterProgressByTopicIdAndUserId(topicId,
              userPrincipal.getId());
      return ResponseEntity.ok(ProgressResponse.builder()
          .success(true)
          .chapterProgressList(chapterProgressList)
          .build());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ProgressResponse.builder()
          .success(false)
          .error(e.getMessage())
          .build());
    }
  }

  @PostMapping("/chapter")
  public ResponseEntity<?> updateChapterProgress(@RequestBody ProgressRequest request,
      @AuthenticationPrincipal UserPrincipal userPrincipal) throws Exception {
    ProgressVo progressVo;

    if (Objects.nonNull(request.getArticleCompleted())) {
      // update article complete
      progressVo = ProgressVo.builder()
          .userId(userPrincipal.getId())
          .chapterId(request.getChapterId())
          .articleCompleted(request.getArticleCompleted())
          .build();
    } else if (Objects.nonNull(request.getQuizCompleted())) {
      // update quiz completed
      progressVo = ProgressVo.builder()
          .userId(userPrincipal.getId())
          .chapterId(request.getChapterId())
          .quizCompleted(request.getQuizCompleted())
          .correct(request.getCorrect())
          .build();
    } else {
      throw new Exception();
    }

    try {
      this.progressService.updateOrCreateChapterProgress(progressVo);
      return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "progress updated successfully"));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
    }
  }
}
