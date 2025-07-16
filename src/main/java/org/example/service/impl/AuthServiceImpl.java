package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.api.request.LoginRequest;
import org.example.api.request.RegisterRequest;
import org.example.api.response.AuthResponse;
import org.example.api.response.UserResponse;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.security.JwtTokenProvider;
import org.example.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;

  @Override
  public AuthResponse login(LoginRequest request) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              request.getEmail(),
              request.getPassword()
          )
      );
    } catch (Exception e) {
      e.printStackTrace(); // ✅ Lihat error aslinya di console
      throw new RuntimeException("Login failed: " + e.getClass().getSimpleName() + " - " + e.getMessage());
    }

    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new RuntimeException("User not found"));

    String token = jwtTokenProvider.generateToken(user.getEmail());

    return new AuthResponse(token, new UserResponse(user.getId(), user.getEmail(), user.getRole()));
  }

  @Override
  public void register(RegisterRequest request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new RuntimeException("Email already registered");
    }

    User user = User.builder()
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(request.getRole())
        .build();

    userRepository.save(user);
  }
}
