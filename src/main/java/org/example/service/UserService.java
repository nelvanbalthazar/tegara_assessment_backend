package org.example.service;

import java.util.List;
import org.example.api.response.UserResponse;

public interface UserService {
  void updateUserRole(Long userId, String newRole);
  List<UserResponse> getAllUsers();

}
