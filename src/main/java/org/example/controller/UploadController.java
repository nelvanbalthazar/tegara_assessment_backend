package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.api.response.CVFileResponse;
import org.example.entity.CVFile;
import org.example.repository.CVFileRepository;
import org.example.service.UploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/cv")
@RequiredArgsConstructor
public class UploadController {

  private final UploadService uploadService;
  private final CVFileRepository cvFileRepository;

  // Upload CV + extract text + save metadata
  @PostMapping("/upload")
  public ResponseEntity<CVFileResponse> uploadAndExtract(
      @RequestParam("file") MultipartFile file,
      @RequestParam("candidateId") Long candidateId
  ) {
    CVFile saved = uploadService.uploadAndExtractText(file, candidateId);
    return ResponseEntity.ok(CVFileResponse.fromEntity(saved));
  }

  // List all uploaded CVs
  @GetMapping
  public ResponseEntity<List<String>> listCvFiles() {
    return ResponseEntity.ok(uploadService.listUploadedFiles());
  }

  // Delete CV from S3
  @DeleteMapping("/{fileName}")
  public ResponseEntity<Void> deleteCvFile(@PathVariable String fileName) {
    uploadService.deleteFile(fileName);
    return ResponseEntity.noContent().build();
  }

  // List all CV metadata by candidate ID
  @GetMapping("/candidate/{id}")
  public ResponseEntity<List<CVFileResponse>> getCVsForCandidate(@PathVariable Long id) {
    List<CVFile> files = uploadService.getCVsForCandidate(id);
    List<CVFileResponse> responses = files.stream()
        .map(CVFileResponse::fromEntity)
        .toList();
    return ResponseEntity.ok(responses);
  }


  // Delete CV by ID
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCvById(@PathVariable Long id) {
    uploadService.deleteCvById(id);
    return ResponseEntity.noContent().build();
  }



}
