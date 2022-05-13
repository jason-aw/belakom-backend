package com.belajarkomputer.belakombackend.controller;

import com.belajarkomputer.belakombackend.exceptions.ResourceNotFoundException;
import com.belajarkomputer.belakombackend.model.request.UpdateUserRequest;
import com.belajarkomputer.belakombackend.model.response.ApiResponse;
import com.belajarkomputer.belakombackend.model.response.AuthResponse;
import com.belajarkomputer.belakombackend.model.vo.UserVo;
import com.belajarkomputer.belakombackend.security.CustomUserDetailsService;
import com.belajarkomputer.belakombackend.security.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping(value = "/api/user")
@AllArgsConstructor
@Slf4j
public class UserController {

  public CustomUserDetailsService userDetailsService;

  @GetMapping("/currentUser")
  public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
    if (Objects.isNull(userPrincipal)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new ApiResponse(false, "Cannot get current user, unauthorized"));
    }
    try {
      UserVo userVo = this.userDetailsService.findUserById(userPrincipal.getId());
      log.info("hai " + userVo);
      return ResponseEntity.ok(AuthResponse.builder()
          .success(true)
          .name(userVo.getName())
          .imageUrl(userVo.getImageUrl())
          .lastSeenChapters(userVo.getLastSeenChapters())
          .currentlyLearningTopic(userVo.getCurrentlyLearningTopic())
          .build());
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new ApiResponse(false, e.getMessage()));
    }
  }

  @PostMapping("/updateUserData")
  public ResponseEntity<?> updateUserData(
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @RequestBody UpdateUserRequest request) {
    if (Objects.isNull(userPrincipal)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new ApiResponse(false, "Cannot get current user, unauthorized"));
    }
    try {
      UserVo userVo = this.userDetailsService.updateUserData(userPrincipal.getId(), request);
      return ResponseEntity.ok(AuthResponse.builder()
          .success(true)
          .name(userVo.getName())
          .imageUrl(userVo.getImageUrl())
          .lastSeenChapters(userVo.getLastSeenChapters())
          .currentlyLearningTopic(userVo.getCurrentlyLearningTopic())
          .build());
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new ApiResponse(false, e.getMessage()));
    }
  }

}