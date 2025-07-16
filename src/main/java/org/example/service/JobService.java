package org.example.service;

import org.example.api.request.JobRequest;
import org.example.api.response.JobResponse;

import java.util.List;

public interface JobService {
  List<JobResponse> getAll();
  JobResponse getById(Long id);
  JobResponse create(JobRequest request);
  JobResponse update(Long id, JobRequest request);
  void delete(Long id);
}
