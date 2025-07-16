package org.example.repository;

import org.example.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
  // Add custom methods if needed later
}
