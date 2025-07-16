package org.example.api.response;

import lombok.*;
import org.example.entity.CVFile;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CVFileResponse {
  private Long id;
  private String fileName;
  private String s3Key;
  private String extractedText;
  private LocalDateTime uploadedAt;
  private Long candidateId;

  public static CVFileResponse fromEntity(CVFile entity) {
    return CVFileResponse.builder()
        .id(entity.getId())
        .fileName(entity.getFileName())
        .s3Key(entity.getS3Key())
        .extractedText(entity.getExtractedText())
        .uploadedAt(entity.getUploadedAt())
        .candidateId(entity.getCandidate().getId())
        .build();
  }
}
