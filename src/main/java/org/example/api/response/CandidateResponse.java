package org.example.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CandidateResponse {
  private Long id;
  private String fullName;
  private String email;
  private String phone;
  private String skills;
  private String education;
  private String experience;
}
