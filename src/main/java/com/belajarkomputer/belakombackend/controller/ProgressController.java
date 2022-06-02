package com.belajarkomputer.belakombackend.controller;

import com.belajarkomputer.belakombackend.model.request.ProgressRequest;
import com.belajarkomputer.belakombackend.model.response.ApiResponse;
import com.belajarkomputer.belakombackend.model.vo.ProgressVo;
import com.belajarkomputer.belakombackend.security.UserPrincipal;
import com.belajarkomputer.belakombackend.service.ProgressService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping(value = "/api/progress")
@AllArgsConstructor
@Slf4j
public class ProgressController {

  private ProgressService progressService;

  @PostMapping("/chapter")
  @Secured("ROLE_USER")
  public ResponseEntity<?> updateChapterProgress(@RequestBody ProgressRequest request,
      @AuthenticationPrincipal UserPrincipal userPrincipal) {
    ProgressVo progressVo = ProgressVo.builder()
        .userId(userPrincipal.getId())
        .chapterId(request.getChapterId())
        .articleCompleted(request.getArticleCompleted())
        .build();

    if (Objects.nonNull(request.getArticleCompleted())) {
      // update article complete
      progressVo.setArticleCompleted(request.getArticleCompleted());
    } else if (Objects.nonNull(request.getQuizCompleted())) {
      // update quiz completed
      progressVo.setQuizCompleted(request.getQuizCompleted());
      progressVo.setCorrect(request.getCorrect());
    } else {
      return ResponseEntity.badRequest().body(ApiResponse.builder().success(false)
          .message("invalid request").build());
    }

    try {
      this.progressService.updateOrCreateChapterProgress(progressVo);
      return ResponseEntity.status(HttpStatus.OK)
          .body(ResponseEntity.badRequest().body(ApiResponse.builder().success(true)
          .build()));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ApiResponse.builder().success(false)
          .message(e.getMessage()).build());
    }
  }
}
