package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.api.response.CandidateScoreResponse;
import org.example.service.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
public class MatchingController {

  private final MatchService matchService;

  @GetMapping("/score")
  public ResponseEntity<List<CandidateScoreResponse>> getTopScores() {
    List<CandidateScoreResponse> scores = matchService.getTopCandidateMatches();
    return ResponseEntity.ok(scores);
  }
}
