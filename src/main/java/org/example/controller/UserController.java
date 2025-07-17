package org.example.controller;

import java.util.List;
import org.example.api.response.UserResponse;
import org.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PutMapping("/{id}/role")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> updateUserRole(
      @PathVariable Long id,
      @RequestBody Map<String, String> request
  ) {
    String newRole = request.get("role");
    if (newRole == null || newRole.isEmpty()) {
      return ResponseEntity.badRequest().body("Role is required.");
    }

    try {
      userService.updateUserRole(id, newRole);
      return ResponseEntity.ok("User role updated to " + newRole);
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }


  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<UserResponse>> getAllUsers() {
    List<UserResponse> users = userService.getAllUsers();
    return ResponseEntity.ok(users);
  }
}
