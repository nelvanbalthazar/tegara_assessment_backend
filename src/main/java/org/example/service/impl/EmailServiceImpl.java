package org.example.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String from;

  @Override
  public void sendOtpEmail(String to, String otp) {
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);

      helper.setFrom(from);
      helper.setTo(to);
      helper.setSubject("Your OTP Code");
      helper.setText("Your OTP code is: <b>" + otp + "</b>", true);

      mailSender.send(message);
      log.info("OTP email sent to {}", to);
    } catch (MessagingException e) {
      log.error("Failed to send OTP email to {}", to, e);
      throw new RuntimeException("Failed to send OTP email");
    }
  }
}
