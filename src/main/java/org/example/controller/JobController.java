package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.api.request.JobRequest;
import org.example.api.response.JobResponse;
import org.example.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

  private final JobService jobService;

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public List<JobResponse> getAll() {
    return jobService.getAll();
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<JobResponse> getById(@PathVariable Long id) {
    return ResponseEntity.ok(jobService.getById(id));
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<JobResponse> create(@RequestBody @Valid JobRequest request) {
    return ResponseEntity.ok(jobService.create(request));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<JobResponse> update(@PathVariable Long id,
      @RequestBody @Valid JobRequest request) {
    return ResponseEntity.ok(jobService.update(id, request));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    jobService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
