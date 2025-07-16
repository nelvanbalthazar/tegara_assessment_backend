package org.example.repository;

import org.example.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
  // Add custom methods if needed later
}
