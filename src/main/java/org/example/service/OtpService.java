package org.example.service;

public interface OtpService {
  String generateOtp(String email);
  boolean verifyOtp(String email, String otp);
  void clearOtp(String email);
}
