package com.belajarkomputer.belakombackend.service.impl;

import com.belajarkomputer.belakombackend.exceptions.BadRequestException;
import com.belajarkomputer.belakombackend.model.entity.Provider;
import com.belajarkomputer.belakombackend.model.entity.Role;
import com.belajarkomputer.belakombackend.model.entity.User;
import com.belajarkomputer.belakombackend.model.request.LoginRequest;
import com.belajarkomputer.belakombackend.model.request.LogoutRequest;
import com.belajarkomputer.belakombackend.model.request.RegisterRequest;
import com.belajarkomputer.belakombackend.repository.UserRepository;
import com.belajarkomputer.belakombackend.security.TokenProvider;
import com.belajarkomputer.belakombackend.service.AuthService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

  private UserRepository userRepository;

  private PasswordEncoder passwordEncoder;

  private AuthenticationManager authenticationManager;

  private TokenProvider tokenProvider;

  @Override
  public User registerUser(RegisterRequest request) {
    if (this.userRepository.existsByEmail(request.getEmail())) {
      throw new BadRequestException("Email address already in use.");
    }

    // Creating user's account
    User user = User.builder()
        .name(request.getName())
        .email(request.getEmail())
        .password(request.getPassword())
        .provider(Provider.LOCAL)
        .role(Role.USER)
        .build();

    user.setPassword(this.passwordEncoder.encode(user.getPassword()));

    return this.userRepository.save(user);
  }

  @Override
  public Map<String, String> authenticateUser(LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginRequest.getEmail(),
            loginRequest.getPassword()
        )
    );

    SecurityContextHolder.getContext().setAuthentication(authentication);

    HashMap<String, String> tokens = new HashMap<>();
    tokens.put("access_token", this.tokenProvider.createAccessToken(authentication));
    tokens.put("refresh_token", this.tokenProvider.createRefreshToken(authentication));
    return tokens;
  }

  @Override
  public Map<String, String> refreshToken(HttpServletRequest request) {
    String refreshToken = this.tokenProvider.getJwtFromRequest(request);
    if (StringUtils.hasText(refreshToken) && this.tokenProvider.validateToken(refreshToken)) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      HashMap<String, String> tokens = new HashMap<>();
      tokens.put("access_token", this.tokenProvider.createAccessToken(auth));
      tokens.put("refresh_token", this.tokenProvider.createRefreshToken(auth));
      return tokens;
    } else {
      throw new BadCredentialsException("Unauthorized");
    }
  }

  @Override
  public void logout(LogoutRequest logoutRequest) {
    log.info("logout request {}", logoutRequest);
    SecurityContextHolder.clearContext();
    try {
      this.tokenProvider.invalidateToken(logoutRequest.getAccessToken());
      this.tokenProvider.invalidateToken(logoutRequest.getRefreshToken());
    } catch (ExpiredJwtException ignored) {}
  }
}
