package com.belajarkomputer.belakombackend.controller;

import com.belajarkomputer.belakombackend.exceptions.BadRequestException;
import com.belajarkomputer.belakombackend.exceptions.ResourceNotFoundException;
import com.belajarkomputer.belakombackend.model.entity.User;
import com.belajarkomputer.belakombackend.model.request.LoginRequest;
import com.belajarkomputer.belakombackend.model.request.LogoutRequest;
import com.belajarkomputer.belakombackend.model.request.RegisterRequest;
import com.belajarkomputer.belakombackend.model.response.ApiResponse;
import com.belajarkomputer.belakombackend.model.response.AuthResponse;
import com.belajarkomputer.belakombackend.repository.UserRepository;
import com.belajarkomputer.belakombackend.security.CurrentUser;
import com.belajarkomputer.belakombackend.security.UserPrincipal;
import com.belajarkomputer.belakombackend.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

  private final UserRepository userRepository;

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    log.info("login request {}", loginRequest);
    try {
      Map<String, String> tokens = this.authService.authenticateUser(loginRequest);
      return ResponseEntity.ok(new AuthResponse(tokens.get("access_token"), tokens.get("refresh_token")));
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

  @GetMapping("/user/me")
  @PreAuthorize("hasRole('USER')")
  public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
    return userRepository.findById(userPrincipal.getId())
        .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
  }

  @GetMapping("/refreshToken")
  public ResponseEntity<?> refreshToken(HttpServletRequest request) {
    try {
      Map<String, String> tokens = this.authService.refreshToken(request);
      return ResponseEntity.ok(new AuthResponse(tokens.get("access_token"), tokens.get("refresh_token")));
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

  @GetMapping("/hello")
  public String hello() {
    return "Hello World!";
  }
}
