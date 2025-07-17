package org.example.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CandidateScoreResponse {
  private String candidateName;
  private String jobTitle;
  private String company;
  private int score;
}
