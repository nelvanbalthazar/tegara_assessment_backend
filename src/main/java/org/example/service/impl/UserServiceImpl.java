package org.example.service.impl;

import java.util.stream.Collectors;
import org.example.api.response.UserResponse;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  private final List<String> allowedRoles = List.of("ADMIN", "RECRUITER", "VIEWER");

  @Override
  public void updateUserRole(Long userId, String newRole) {
    String roleUpper = newRole.toUpperCase();

    if (!allowedRoles.contains(roleUpper)) {
      throw new IllegalArgumentException("Invalid role: " + newRole);
    }

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));

    user.setRole(roleUpper);
    userRepository.save(user);
  }

  @Override
  public List<UserResponse> getAllUsers() {
    return userRepository.findAll().stream()
        .map(user -> new UserResponse(user.getId(), user.getEmail(), user.getRole()))
        .collect(Collectors.toList());
  }

}
