package com.belajarkomputer.belakombackend.controller;

import com.belajarkomputer.belakombackend.exceptions.ResourceNotFoundException;
import com.belajarkomputer.belakombackend.model.entity.Chapter;
import com.belajarkomputer.belakombackend.model.request.UpdateUserRequest;
import com.belajarkomputer.belakombackend.model.response.ApiResponse;
import com.belajarkomputer.belakombackend.model.vo.UserChapterHistory;
import com.belajarkomputer.belakombackend.model.vo.UserVo;
import com.belajarkomputer.belakombackend.security.CustomUserDetailsService;
import com.belajarkomputer.belakombackend.security.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
          .body(ApiResponse.builder().success(false)
              .message("Cannot get current user, unauthorized").build());
    }
    try {
      UserVo userVo = this.userDetailsService.findUserById(userPrincipal.getId());
      List<String> roles = userPrincipal.getAuthorities()
          .stream()
          .map(GrantedAuthority::getAuthority)
          .collect(Collectors.toList());
      userVo.setRoles(roles);
      return ResponseEntity.ok(ApiResponse.builder().success(true).value(userVo).build());
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(ApiResponse.builder().success(false).message(e.getMessage()).build());
    }
  }

  @GetMapping("/currentUserChapterHistory")
  public ResponseEntity<?> getCurrentUserChapter(@AuthenticationPrincipal UserPrincipal userPrincipal) {
    if (Objects.isNull(userPrincipal)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(ApiResponse.builder().success(false)
              .message("Cannot get current user, unauthorized").build());
    }
    try {
      UserVo userVo = this.userDetailsService.findUserById(userPrincipal.getId());
      List<UserChapterHistory> chapterUsers = this.userDetailsService.getUserChapterData(userVo);
      return ResponseEntity.ok(ApiResponse.builder().success(true).value(chapterUsers).build());
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(ApiResponse.builder().success(false).message(e.getMessage()).build());
    }
  }

  @PostMapping("/updateUserData")
  public ResponseEntity<?> updateUserData(
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @RequestBody UpdateUserRequest request) {
    if (Objects.isNull(userPrincipal)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(ApiResponse.builder().success(false)
              .message("Cannot get current user, unauthorized").build());
    }
    try {
      log.info("UpdateUserData req: {}, user: {}", request, userPrincipal);
      this.userDetailsService.updateUserData(userPrincipal.getId(), request);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.builder().success(false).message(e.getMessage()).build());
    }
  }

}
