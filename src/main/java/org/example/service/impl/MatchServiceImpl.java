package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.api.response.CandidateScoreResponse;
import org.example.entity.Candidate;
import org.example.entity.Job;
import org.example.repository.CandidateRepository;
import org.example.repository.JobRepository;
import org.example.service.MatchService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

  private final CandidateRepository candidateRepository;
  private final JobRepository jobRepository;

  @Override
  public List<CandidateScoreResponse> getTopCandidateMatches() {
    List<Candidate> candidates = candidateRepository.findAll();
    List<Job> jobs = jobRepository.findAll();

    List<CandidateScoreResponse> allScores = new ArrayList<>();

    for (Candidate c : candidates) {
      for (Job j : jobs) {
        int score = computeScore(c, j);
        if (score >= 70) {
          allScores.add(new CandidateScoreResponse(
              c.getFullName(),
              j.getTitle(),
              j.getCompany(),
              score
          ));
        }
      }
    }

    // Sort descending and limit to top 50
    return allScores.stream()
        .sorted(Comparator.comparingInt(CandidateScoreResponse::getScore).reversed())
        .limit(50)
        .collect(Collectors.toList());
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

    if (c.getEducation() != null && j.getDescription() != null &&
        j.getDescription().toLowerCase().contains(c.getEducation().toLowerCase())) {
      score += 10;
    }

    if (c.getExperience() != null && j.getDescription() != null &&
        j.getDescription().toLowerCase().contains(c.getExperience().toLowerCase())) {
      score += 10;
    }

    return score;
  }
}
