package com.belajarkomputer.belakombackend.controller;

import com.belajarkomputer.belakombackend.exceptions.BadRequestException;
import com.belajarkomputer.belakombackend.model.entity.User;
import com.belajarkomputer.belakombackend.model.request.LoginRequest;
import com.belajarkomputer.belakombackend.model.request.LogoutRequest;
import com.belajarkomputer.belakombackend.model.request.RegisterRequest;
import com.belajarkomputer.belakombackend.model.request.ResetPasswordRequest;
import com.belajarkomputer.belakombackend.model.response.ApiResponse;
import com.belajarkomputer.belakombackend.model.response.AuthResponse;
import com.belajarkomputer.belakombackend.model.vo.UserVo;
import com.belajarkomputer.belakombackend.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "/api/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    log.info("login request {}", loginRequest);
    try {
      UserVo userVo = this.authService.authenticateUser(loginRequest);
      log.info("eek" + userVo);
      return ResponseEntity.ok(AuthResponse.builder()
          .success(true)
          .accessToken(userVo.getAccessToken())
          .refreshToken(userVo.getRefreshToken())
          .roles(userVo.getRoles())
          .email(userVo.getEmail())
          .userId(userVo.getUserId())
          .build());
    } catch (DisabledException e) {
      e.printStackTrace();
      return ResponseEntity.internalServerError().body(ApiResponse.builder()
          .success(false).message("Account is disabled").build());
    } catch (BadCredentialsException e) {
      return ResponseEntity.status(401).body(ApiResponse.builder()
          .success(false).message("Bad credentials").build());
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(500).body(ApiResponse.builder()
          .success(false).message("Unspecified").build());
    }
  }

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request) {
    User result;
    try {
      result = this.authService.registerUser(request);
      URI location = ServletUriComponentsBuilder
          .fromCurrentContextPath().path("/api/auth/user/me")
          .buildAndExpand(result.getId()).toUri();

      return ResponseEntity.created(location).body(ApiResponse.builder()
          .success(true).message("User registered successfully").build());
    } catch (BadRequestException ex) {
      return ResponseEntity.badRequest().body(ApiResponse.builder()
          .success(false).message(ex.getMessage()).build());
    }
  }

  @GetMapping("/refreshToken")
  public ResponseEntity<?> refreshToken(HttpServletRequest request) {
    log.info("refresh token {}", request);
    try {
      UserVo userVo = this.authService.refreshToken(request);
      return ResponseEntity.ok(AuthResponse.builder()
          .success(true)
          .accessToken(userVo.getAccessToken())
          .refreshToken(userVo.getRefreshToken())
          .roles(userVo.getRoles())
          .email(userVo.getEmail())
          .build());
    } catch (BadCredentialsException e) {
      return ResponseEntity.status(400).body(ApiResponse.builder()
          .success(false).message("Bad credentials").build());
    }
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(@RequestBody LogoutRequest request) {
    log.info("logout request {}", request);
    this.authService.logout(request);
    return ResponseEntity.ok().body(ApiResponse.builder()
        .success(true).build());
  }

  @PostMapping("/forgotPassword")
  public ResponseEntity<?> forgotPassword(@RequestBody LoginRequest request) {
    log.info("forgot password for {}", request.getEmail());
    try {
      this.authService.forgotPassword(request.getEmail());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ApiResponse.builder()
          .success(false).message(e.getMessage()).build());
    }
    return ResponseEntity.ok(ApiResponse.builder().success(true).message("Please check your email").build());
  }

  @PostMapping("/resetPassword")
  public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
    log.info("reset password");
    try {
      this.authService.resetPassword(request);
      return ResponseEntity.ok().body(ApiResponse.builder()
          .success(true).message("Password reset successful").build());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ApiResponse.builder()
          .success(false).message(e.getMessage()).build());
    }
  }
}
