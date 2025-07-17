package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.api.request.LoginRequest;
import org.example.api.request.OtpVerificationRequest;
import org.example.api.request.RegisterRequest;
import org.example.api.response.AuthResponse;
import org.example.api.response.UserResponse;
import org.example.entity.User;
import org.example.service.AuthService;
import org.example.service.EmailService;
import org.example.service.OtpService;
import org.example.service.UserService;
import org.example.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  private final AuthenticationManager authenticationManager;
  private final OtpService otpService;
  private final EmailService emailService;
  private final JwtUtil jwtUtil;
  private final UserService userService;

  // Step 1: Login (No Token Yet)
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
      );

      // Send OTP if login is successful
      String otp = otpService.generateOtp(request.getEmail());
      emailService.sendOtpEmail(request.getEmail(), otp);

      return ResponseEntity.ok(Map.of("status", "OTP_SENT"));
    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
  }

  // Step 2: OTP Verification → Return JWT
  @PostMapping("/verify-otp")
  public ResponseEntity<?> verifyOtp(@RequestBody OtpVerificationRequest request) {
    boolean valid = otpService.verifyOtp(request.getEmail(), request.getOtp());
    if (!valid) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP");
    }

    User user = userService.loadUserByEmail(request.getEmail())
        .orElseThrow(() -> new RuntimeException("Candidate not found"));
    String token = jwtUtil.generateToken(user);
    otpService.clearOtp(request.getEmail());

    UserResponse userResponse = new UserResponse(user.getId(), user.getEmail(), user.getRole());

    return ResponseEntity.ok(new AuthResponse(token, userResponse));
  }

  // Registration remains as-is
  @PostMapping("/register")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequest request) {
    authService.register(request);
    return ResponseEntity.ok().build();
  }
}
