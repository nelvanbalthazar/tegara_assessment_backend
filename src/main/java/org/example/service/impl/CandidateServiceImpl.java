package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.api.request.CandidateRequest;
import org.example.api.response.CandidateResponse;
import org.example.entity.Candidate;
import org.example.repository.CandidateRepository;
import org.example.service.CandidateService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {

  private final CandidateRepository candidateRepository;

  @Override
  public List<CandidateResponse> getAll() {
    return candidateRepository.findAll().stream()
        .map(this::toResponse)
        .collect(Collectors.toList());
  }

  @Override
  public CandidateResponse getById(Long id) {
    return candidateRepository.findById(id)
        .map(this::toResponse)
        .orElseThrow(() -> new RuntimeException("Candidate not found"));
  }

  @Override
  public CandidateResponse create(CandidateRequest request) {
    Candidate candidate = Candidate.builder()
        .fullName(request.getFullName())
        .email(request.getEmail())
        .phone(request.getPhone())
        .skills(request.getSkills())
        .education(request.getEducation())
        .experience(request.getExperience())
        .build();

    candidateRepository.save(candidate);
    return toResponse(candidate);
  }

  @Override
  public CandidateResponse update(Long id, CandidateRequest request) {
    Candidate candidate = candidateRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Candidate not found"));

    candidate.setFullName(request.getFullName());
    candidate.setEmail(request.getEmail());
    candidate.setPhone(request.getPhone());
    candidate.setSkills(request.getSkills());
    candidate.setEducation(request.getEducation());
    candidate.setExperience(request.getExperience());

    candidateRepository.save(candidate);
    return toResponse(candidate);
  }

  @Override
  public void delete(Long id) {
    candidateRepository.deleteById(id);
  }

  private CandidateResponse toResponse(Candidate candidate) {
    return new CandidateResponse(
        candidate.getId(),
        candidate.getFullName(),
        candidate.getEmail(),
        candidate.getPhone(),
        candidate.getSkills(),
        candidate.getEducation(),
        candidate.getExperience()
    );
  }
}
