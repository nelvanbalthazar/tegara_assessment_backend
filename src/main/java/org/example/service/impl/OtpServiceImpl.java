package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.service.OtpService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@Slf4j
public class OtpServiceImpl implements OtpService {

  private final Map<String, String> otpStore = new HashMap<>();
  private final Random random = new Random();

  @Override
  public String generateOtp(String email) {
    String otp = String.format("%06d", random.nextInt(999999));
    otpStore.put(email, otp);
    log.info("Generated OTP for {}: {}", email, otp); // Optional: log for debugging
    return otp;
  }

  @Override
  public boolean verifyOtp(String email, String otp) {
    String storedOtp = otpStore.get(email);
    return storedOtp != null && storedOtp.equals(otp);
  }

  @Override
  public void clearOtp(String email) {
    otpStore.remove(email);
  }
}
