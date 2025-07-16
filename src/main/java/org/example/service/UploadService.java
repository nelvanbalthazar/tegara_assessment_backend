package org.example.service;

import org.example.entity.CVFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UploadService {
  CVFile uploadAndExtractText(MultipartFile file, Long candidateId);
  List<String> listUploadedFiles();
  void deleteFile(String fileName);
  void deleteCvById(Long id);
  List<CVFile> getCVsForCandidate(Long candidateId);


}
