package org.example.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cv_files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CVFile {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "file_name", nullable = false)
  private String fileName;

  @Column(name = "s3_key", nullable = false)
  private String s3Key;

  @Column(name = "extracted_text", columnDefinition = "TEXT")
  private String extractedText;

  @Column(name = "uploaded_at", nullable = false)
  private LocalDateTime uploadedAt;

  @ManyToOne
  @JoinColumn(name = "candidate_id", nullable = false)
  private Candidate candidate;
}
