package com.belajarkomputer.belakombackend.service.impl;

import com.belajarkomputer.belakombackend.config.AppProperties;
import com.belajarkomputer.belakombackend.exceptions.BadRequestException;
import com.belajarkomputer.belakombackend.model.entity.Provider;
import com.belajarkomputer.belakombackend.model.entity.Role;
import com.belajarkomputer.belakombackend.model.entity.User;
import com.belajarkomputer.belakombackend.model.request.LoginRequest;
import com.belajarkomputer.belakombackend.model.request.LogoutRequest;
import com.belajarkomputer.belakombackend.model.request.RegisterRequest;
import com.belajarkomputer.belakombackend.model.request.ResetPasswordRequest;
import com.belajarkomputer.belakombackend.model.vo.UserVo;
import com.belajarkomputer.belakombackend.repository.UserRepository;
import com.belajarkomputer.belakombackend.security.UserPrincipal;
import com.belajarkomputer.belakombackend.service.AuthService;
import com.belajarkomputer.belakombackend.service.MailService;
import com.belajarkomputer.belakombackend.utils.TokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

  private UserRepository userRepository;

  private PasswordEncoder passwordEncoder;

  private AuthenticationManager authenticationManager;

  private TokenProvider tokenProvider;

  private AppProperties properties;

  private MailService mailService;

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
  public UserVo authenticateUser(LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginRequest.getEmail(),
            loginRequest.getPassword()
        )
    );

    SecurityContextHolder.getContext().setAuthentication(authentication);
    UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
    List<String> roles = principal.getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());
    return UserVo.builder()
        .email(principal.getEmail())
        .userId(principal.getId())
        .accessToken(this.tokenProvider.createAccessToken(authentication))
        .refreshToken(this.tokenProvider.createRefreshToken(authentication))
        .roles(roles)
        .build();
  }

  @Override
  public UserVo refreshToken(HttpServletRequest request) {
    String refreshToken = this.tokenProvider.getJwtFromRequest(request);
    if (StringUtils.hasText(refreshToken) && this.tokenProvider.validateToken(refreshToken)) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
      List<String> roles = principal.getAuthorities()
          .stream()
          .map(GrantedAuthority::getAuthority)
          .collect(Collectors.toList());
      return UserVo.builder()
          .email(principal.getEmail())
          .accessToken(this.tokenProvider.createAccessToken(auth))
          .refreshToken(refreshToken)
          .roles(roles)
          .build();
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
      if (StringUtils.hasLength(logoutRequest.getRefreshToken())) {
        this.tokenProvider.invalidateToken(logoutRequest.getRefreshToken());
      }
    } catch (ExpiredJwtException ignored) {
    }
  }

  @Override
  public void forgotPassword(String email) throws Exception {
    User user = this.userRepository.findByEmail(email);

    if (Objects.isNull(user)) {
      throw new UsernameNotFoundException("User not found with email : " + email);
    }
    if (Provider.GOOGLE.equals(user.getProvider())) {
      throw new BadRequestException("This account is logged in via google, can't reset password here");
    }

    String token = UUID.randomUUID().toString();
    user.setResetPasswordToken(token);
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.SECOND, (int) this.properties.getAuth().getPasswordResetTokenExpirationSec());
    user.setResetPasswordTokenExpiry(calendar.getTime());
    this.userRepository.save(user);

    this.mailService.sendPasswordResetEmail(email, token);
  }


  @Override
  public void resetPassword(ResetPasswordRequest request) {
    User user = this.userRepository.findUserByResetPasswordToken(request.getToken());
    if (Objects.isNull(user)) {
      throw new UsernameNotFoundException("User not found");
    }
    if (Calendar.getInstance().after(user.getResetPasswordTokenExpiry())) {
      user.setResetPasswordToken(null);
      user.setResetPasswordTokenExpiry(null);
      this.userRepository.save(user);
      throw new BadRequestException("Token expired");
    }
    user.setPassword(this.passwordEncoder.encode(request.getPassword()));
    user.setResetPasswordToken(null);
    user.setResetPasswordTokenExpiry(null);
    this.userRepository.save(user);
  }
}
