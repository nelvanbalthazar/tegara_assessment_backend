package org.example.repository;

import org.example.entity.CVFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CVFileRepository extends JpaRepository<CVFile, Long> {
  List<CVFile> findAllByCandidateId(Long candidateId);
}
