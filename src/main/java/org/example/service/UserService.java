package org.example.service;

import java.util.List;
import java.util.Optional;
import org.example.api.response.UserResponse;
import org.example.entity.User;

public interface UserService {
  void updateUserRole(Long userId, String newRole);
  List<UserResponse> getAllUsers();
  Optional<User> loadUserByEmail(String email);

}
