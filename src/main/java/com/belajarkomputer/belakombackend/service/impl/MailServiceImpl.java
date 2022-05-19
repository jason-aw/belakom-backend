package com.belajarkomputer.belakombackend.service.impl;

import com.belajarkomputer.belakombackend.config.AppProperties;
import com.belajarkomputer.belakombackend.service.MailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@Slf4j
@AllArgsConstructor
public class MailServiceImpl implements MailService {
  private JavaMailSender javaMailSender;
  private AppProperties appProperties;

  @Async
  public void sendMail(String to, String body) throws MessagingException {
    MimeMessage message = this.javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
    helper.setFrom("belakom123@gmail.com");
    helper.setTo(to);
    helper.setSubject("Forgot Password Confirmation [BELAKOM]");
    helper.setText(body, true);
    this.javaMailSender.send(message);
  }

  @Override
  public void sendPasswordResetEmail(String to, String token) throws MessagingException {
    this.sendMail(to, this.constructEmail(this.appProperties.getResetPasswordPage() + token));
  }

  private String constructEmail(String url) {
    return String.format("<div>\n"
        + "  <h1>Password Reset Confirmation</h1>\n"
        + "  <hr>\n"
        + "  <p>Please click the link below to reset password (valid for 1 hour): </p>\n"
        + "  <br>\n"
        + "  <p>%s</p>\n"
        + "</div>", url);
  }
}
