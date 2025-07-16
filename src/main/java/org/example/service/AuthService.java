package org.example.service;

import org.example.api.request.LoginRequest;
import org.example.api.request.RegisterRequest;
import org.example.api.response.AuthResponse;

public interface AuthService {
  AuthResponse login(LoginRequest request);
  void register(RegisterRequest request);
}
