package org.example.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CandidateRequest {

  @NotBlank
  private String fullName;

  private String email;
  private String phone;
  private String skills;
  private String education;
  private String experience;
}
