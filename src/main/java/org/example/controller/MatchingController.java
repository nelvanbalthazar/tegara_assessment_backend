package org.example.controller;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.api.response.CandidateScoreResponse;
import org.example.entity.Candidate;
import org.example.entity.Job;
import org.example.repository.CandidateRepository;
import org.example.repository.JobRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
public class MatchingController {

  private final CandidateRepository candidateRepository;
  private final JobRepository jobRepository;

  @GetMapping("/score")
  public ResponseEntity<List<CandidateScoreResponse>> getCandidateScores() {
    List<Candidate> candidates = candidateRepository.findAll();
    List<Job> jobs = jobRepository.findAll();

    List<CandidateScoreResponse> scores = new ArrayList<>();

    for (Candidate c : candidates) {
      for (Job j : jobs) {
        int score = computeScore(c, j);
        scores.add(new CandidateScoreResponse(c.getFullName(), j.getTitle(), j.getCompany(), score));
      }
    }

    return ResponseEntity.ok(scores);
  }

  private int computeScore(Candidate c, Job j) {
    int score = 0;
    if (c.getSkills() != null && j.getDescription() != null) {
      for (String skill : c.getSkills().toLowerCase().split("[,\\s]+")) {
        if (j.getDescription().toLowerCase().contains(skill)) {
          score += 10;
        }
      }
    }
    if (c.getEducation() != null && j.getDescription() != null) {
      if (j.getDescription().toLowerCase().contains(c.getEducation().toLowerCase())) {
        score += 10;
      }
    }
    if (c.getExperience() != null && j.getDescription() != null) {
      if (j.getDescription().toLowerCase().contains(c.getExperience().toLowerCase())) {
        score += 10;
      }
    }
    return score;
  }
}
