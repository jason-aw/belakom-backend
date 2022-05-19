package com.belajarkomputer.belakombackend.service;

public interface MailService {
  void sendPasswordResetEmail(String to, String token) throws Exception;
}
