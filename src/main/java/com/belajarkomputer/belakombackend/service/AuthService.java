package com.belajarkomputer.belakombackend.service;

import com.belajarkomputer.belakombackend.model.entity.User;
import com.belajarkomputer.belakombackend.model.request.LoginRequest;
import com.belajarkomputer.belakombackend.model.request.LogoutRequest;
import com.belajarkomputer.belakombackend.model.request.RegisterRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface AuthService {
  User registerUser(RegisterRequest request);

  Map<String, String> authenticateUser(LoginRequest loginRequest);

  Map<String, String> refreshToken(HttpServletRequest request);

  void logout(LogoutRequest request);
}
