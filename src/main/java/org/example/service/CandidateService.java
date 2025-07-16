package org.example.service;

import org.example.api.request.CandidateRequest;
import org.example.api.response.CandidateResponse;

import java.util.List;

public interface CandidateService {
  List<CandidateResponse> getAll();
  CandidateResponse getById(Long id);
  CandidateResponse create(CandidateRequest request);
  CandidateResponse update(Long id, CandidateRequest request);
  void delete(Long id);
}
