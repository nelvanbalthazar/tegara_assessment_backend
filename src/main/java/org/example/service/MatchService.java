package org.example.service;

import org.example.api.response.CandidateScoreResponse;

import java.util.List;

public interface MatchService {
  List<CandidateScoreResponse> getTopCandidateMatches();
}
