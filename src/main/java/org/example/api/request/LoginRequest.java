package org.example.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

  @Email
  @NotBlank
  private String email;

  @NotBlank
  private String password;
}
