package com.belajarkomputer.belakombackend.controller;

import com.belajarkomputer.belakombackend.exceptions.BadRequestException;
import com.belajarkomputer.belakombackend.exceptions.ResourceNotFoundException;
import com.belajarkomputer.belakombackend.model.entity.User;
import com.belajarkomputer.belakombackend.model.request.LoginRequest;
import com.belajarkomputer.belakombackend.model.request.LogoutRequest;
import com.belajarkomputer.belakombackend.model.request.RegisterRequest;
import com.belajarkomputer.belakombackend.model.response.ApiResponse;
import com.belajarkomputer.belakombackend.model.response.AuthResponse;
import com.belajarkomputer.belakombackend.model.vo.UserVo;
import com.belajarkomputer.belakombackend.security.CustomUserDetailsService;
import com.belajarkomputer.belakombackend.security.UserPrincipal;
import com.belajarkomputer.belakombackend.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.Objects;

@RestController
@RequestMapping(value = "/api/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

  private final AuthService authService;
  private final CustomUserDetailsService userDetailsService;

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    log.info("login request {}", loginRequest);
    try {
      UserVo userVo = this.authService.authenticateUser(loginRequest);
      return ResponseEntity.ok(AuthResponse.builder()
          .success(true)
          .accessToken(userVo.getAccessToken())
          .refreshToken(userVo.getRefreshToken())
          .roles(userVo.getRoles())
          .email(userVo.getEmail())
          .currentlyLearningTopic(userVo.getCurrentlyLearningTopic())
          .build());
    } catch (DisabledException e) {
      e.printStackTrace();
      return ResponseEntity.internalServerError().body(AuthResponse.builder()
          .success(false).error("Account is disabled").build());
    } catch (BadCredentialsException e) {
      return ResponseEntity.status(401).body(AuthResponse.builder()
          .success(false).error("Bad credentials").build());
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(500).body(AuthResponse.builder()
          .success(false).error("Unspecified").build());
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

      return ResponseEntity.created(location)
          .body(new ApiResponse(true, "User registered successfully"));
    } catch (BadRequestException ex) {
      return ResponseEntity.badRequest()
          .body(new ApiResponse(false, ex.getMessage()));
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
      return ResponseEntity.status(400).body(AuthResponse.builder()
          .success(false).error("Bad credentials").build());
    }
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(@RequestBody LogoutRequest request) {
    log.info("logout request {}", request);
    this.authService.logout(request);
    return ResponseEntity.ok().body(null);
  }
}
