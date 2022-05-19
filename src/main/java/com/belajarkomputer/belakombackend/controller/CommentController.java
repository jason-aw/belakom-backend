package com.belajarkomputer.belakombackend.controller;

import com.belajarkomputer.belakombackend.model.request.CommentRequest;
import com.belajarkomputer.belakombackend.model.request.EditCommentRequest;
import com.belajarkomputer.belakombackend.model.response.ApiResponse;
import com.belajarkomputer.belakombackend.model.vo.CommentsVo;
import com.belajarkomputer.belakombackend.security.UserPrincipal;
import com.belajarkomputer.belakombackend.service.CommentService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/comment")
@AllArgsConstructor
@Slf4j
public class CommentController {

  private CommentService commentService;

  @GetMapping("/{chapterId}")
  public ResponseEntity<?> getCommentsByChapterId(@PathVariable String chapterId) {
    try {
      CommentsVo commentsVo = this.commentService.getCommentsByChapterId(chapterId);
      return ResponseEntity.ok(ApiResponse.builder()
          .success(true).value(commentsVo).build());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ApiResponse.builder()
          .success(false).message(e.getMessage()).build());
    }
  }

  @Secured({"ROLE_USER", "ROLE_ADMIN"})
  @PostMapping
  public ResponseEntity<?> createComment(@AuthenticationPrincipal UserPrincipal userPrincipal,
      @RequestBody CommentRequest request) {
    this.commentService.createComment(userPrincipal.getId(), request);
    return ResponseEntity.ok(ApiResponse.builder().success(true).build());
  }

  @Secured({"ROLE_USER", "ROLE_ADMIN"})
  @PutMapping
  public ResponseEntity<?> editComment(@AuthenticationPrincipal UserPrincipal userPrincipal,
      @RequestBody EditCommentRequest request) {
    try {
      this.commentService.editComment(userPrincipal, request);
      return ResponseEntity.ok(ApiResponse.builder().success(true).build());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ApiResponse.builder()
          .success(false).message(e.getMessage()).build());
    }
  }

  @Secured({"ROLE_USER", "ROLE_ADMIN"})
  @DeleteMapping("/{commentId}")
  public ResponseEntity<?> deleteComment(@AuthenticationPrincipal UserPrincipal userPrincipal,
      @PathVariable String commentId) {
    try {
      this.commentService.deleteComment(userPrincipal, commentId);
      return ResponseEntity.ok(ApiResponse.builder().success(true).build());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ApiResponse.builder()
          .success(false).message(e.getMessage()).build());
    }
  }
}
