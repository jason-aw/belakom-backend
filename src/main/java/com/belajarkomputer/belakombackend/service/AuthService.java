package com.belajarkomputer.belakombackend.service;

import com.belajarkomputer.belakombackend.model.entity.User;
import com.belajarkomputer.belakombackend.model.request.LoginRequest;
import com.belajarkomputer.belakombackend.model.request.LogoutRequest;
import com.belajarkomputer.belakombackend.model.request.RegisterRequest;
import com.belajarkomputer.belakombackend.model.request.ResetPasswordRequest;
import com.belajarkomputer.belakombackend.model.vo.UserVo;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {
  User registerUser(RegisterRequest request);

  UserVo authenticateUser(LoginRequest loginRequest);

  UserVo refreshToken(HttpServletRequest request);

  void logout(LogoutRequest request);

  void forgotPassword(String email) throws Exception;

  void resetPassword(ResetPasswordRequest request);
}
