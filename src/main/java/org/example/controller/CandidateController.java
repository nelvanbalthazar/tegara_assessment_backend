package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.api.request.CandidateRequest;
import org.example.api.response.CandidateResponse;
import org.example.service.CandidateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
@RequiredArgsConstructor
public class CandidateController {

  private final CandidateService candidateService;

  @GetMapping
  public List<CandidateResponse> getAll() {
    return candidateService.getAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<CandidateResponse> getById(@PathVariable Long id) {
    return ResponseEntity.ok(candidateService.getById(id));
  }

  @PostMapping
  public ResponseEntity<CandidateResponse> create(@RequestBody @Valid CandidateRequest request) {
    return ResponseEntity.ok(candidateService.create(request));
  }

  @PutMapping("/{id}")
  public ResponseEntity<CandidateResponse> update(@PathVariable Long id,
      @RequestBody @Valid CandidateRequest request) {
    return ResponseEntity.ok(candidateService.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    candidateService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
