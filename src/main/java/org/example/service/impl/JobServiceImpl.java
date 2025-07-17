package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.api.request.JobRequest;
import org.example.api.response.JobResponse;
import org.example.entity.Job;
import org.example.repository.JobRepository;
import org.example.service.JobService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

  private final JobRepository jobRepository;

  @Override
  public List<JobResponse> getAll() {
    return jobRepository.findAll().stream()
        .map(this::toResponse)
        .collect(Collectors.toList());
  }

  @Override
  public JobResponse getById(Long id) {
    return jobRepository.findById(id)
        .map(this::toResponse)
        .orElseThrow(() -> new RuntimeException("Job not found"));
  }

  @Override
  public JobResponse create(JobRequest request) {
    Job job = Job.builder()
        .title(request.getTitle())
        .description(request.getDescription())
        .company(request.getCompany())
        .location(request.getLocation())
        .build();

    jobRepository.save(job);
    return toResponse(job);
  }

  @Override
  public JobResponse update(Long id, JobRequest request) {
    Job job = jobRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Job not found"));

    job.setTitle(request.getTitle());
    job.setDescription(request.getDescription());
    job.setCompany(request.getCompany());
    job.setLocation(request.getLocation());

    jobRepository.save(job);
    return toResponse(job);
  }

  @Override
  public void delete(Long id) {
    jobRepository.deleteById(id);
  }

  private JobResponse toResponse(Job job) {
    return new JobResponse(
        job.getId(),
        job.getTitle(),
        job.getDescription(),
        job.getCompany(),
        job.getLocation(),
        job.getCompany()
    );
  }
}
