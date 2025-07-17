package org.example.service.impl;

import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.example.config.AppProperties;
import org.example.entity.CVFile;
import org.example.entity.Candidate;
import org.example.repository.CVFileRepository;
import org.example.repository.CandidateRepository;
import org.example.service.UploadService;
import org.example.service.impl.utils.Utils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UploadServiceImpl implements UploadService {

  private final AppProperties appProperties;
  private final CVFileRepository cvFileRepository;
  private final CandidateRepository candidateRepository;

  private S3Client createS3Client() {
    return S3Client.builder()
        .region(Region.of(appProperties.getS3Region()))
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(
                    appProperties.getAccessKey(),
                    appProperties.getSecretKey()
                )
            )
        )
        .build();
  }

  @Override
  public CVFile uploadAndExtractText(MultipartFile file, Long candidateId) {
    try {
      String originalFileName = file.getOriginalFilename();
      if (originalFileName == null) throw new RuntimeException("File name is missing");

      String fileName = System.currentTimeMillis() + "_" + originalFileName;
      String fileExtension = getFileExtension(originalFileName).toLowerCase();

      // Upload to S3
      S3Client s3 = createS3Client();
      s3.putObject(
          PutObjectRequest.builder()
              .bucket(appProperties.getS3Bucket())
              .key(fileName)
              .build(),
          RequestBody.fromBytes(file.getBytes())
      );

      String extractedText;

      if (fileExtension.equals("pdf")) {
        extractedText = extractFromPdfS3(fileName);
      } else if (fileExtension.equals("docx")) {
        extractedText = extractFromDocx(file);
      } else if (fileExtension.equals("txt")) {
        extractedText = new String(file.getBytes(), StandardCharsets.UTF_8);
      } else {
        throw new RuntimeException("Unsupported file type: " + fileExtension);
      }

      // Extract structured fields
      String skills = Utils.extractSection(extractedText, "Skills");
      String education = Utils.extractSection(extractedText, "Education");
      String experience = Utils.extractSection(extractedText, "Experience");

      // Update candidate
      Candidate candidate = candidateRepository.findById(candidateId)
          .orElseThrow(() -> new RuntimeException("Candidate not found"));

      candidate.setSkills(skills);
      candidate.setEducation(education);
      candidate.setExperience(experience);
      candidate.setSourceFileName(originalFileName);
      candidateRepository.save(candidate);

      // Save CV record
      CVFile cvFile = CVFile.builder()
          .fileName(originalFileName)
          .s3Key(fileName)
          .extractedText(extractedText)
          .uploadedAt(LocalDateTime.now())
          .candidate(candidate)
          .build();

      return cvFileRepository.save(cvFile);

    } catch (IOException e) {
      throw new RuntimeException("File read error", e);
    } catch (S3Exception | TextractException e) {
      throw new RuntimeException("AWS error: " + e.awsErrorDetails().errorMessage(), e);
    } catch (InterruptedException e) {
      throw new RuntimeException("AWS error: " + e.getMessage(), e);
    }
  }

  private String extractFromPdfS3(String s3Key) throws InterruptedException {
    TextractClient textract = createTextractClient();

    StartDocumentAnalysisRequest startRequest = StartDocumentAnalysisRequest.builder()
        .documentLocation(DocumentLocation.builder()
            .s3Object(software.amazon.awssdk.services.textract.model.S3Object.builder()
                    .bucket(appProperties.getS3Bucket())
                    .name(s3Key)
                    .build())
            .build())
        .featureTypes(FeatureType.FORMS, FeatureType.TABLES)
        .build();

    StartDocumentAnalysisResponse startResponse = textract.startDocumentAnalysis(startRequest);
    String jobId = startResponse.jobId();

    GetDocumentAnalysisResponse response;

    while (true) {
      Thread.sleep(2000);
      response = textract.getDocumentAnalysis(GetDocumentAnalysisRequest.builder()
          .jobId(jobId)
          .build());

      String status = response.jobStatusAsString();
      if ("SUCCEEDED".equals(status)) break;
      if ("FAILED".equals(status)) throw new RuntimeException("Textract failed");
    }

    return response.blocks().stream()
        .filter(block -> block.blockType().equals(BlockType.LINE))
        .map(Block::text)
        .collect(Collectors.joining("\n"));
  }


  private String extractFromDocx(MultipartFile file) throws IOException {
    try (XWPFDocument doc = new XWPFDocument(file.getInputStream())) {
      return doc.getParagraphs().stream()
          .map(XWPFParagraph::getText)
          .collect(Collectors.joining("\n"));
    }
  }

  private TextractClient createTextractClient() {
    return TextractClient.builder()
        .region(Region.of(appProperties.getTextractRegion()))
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(
                    appProperties.getAccessKey(),
                    appProperties.getSecretKey()
                )
            )
        )
        .build();
  }


  private String getFileExtension(String filename) {
    int dotIndex = filename.lastIndexOf('.');
    return dotIndex != -1 ? filename.substring(dotIndex + 1) : "";
  }




  @Override
  public List<String> listUploadedFiles() {
    S3Client s3 = createS3Client();

    ListObjectsV2Response response = s3.listObjectsV2(ListObjectsV2Request.builder()
        .bucket(appProperties.getS3Bucket())
        .build());

    return response.contents().stream()
        .map(S3Object::key)
        .collect(Collectors.toList());
  }

  @Override
  public void deleteFile(String fileName) {
    S3Client s3 = createS3Client();

    s3.deleteObject(DeleteObjectRequest.builder()
        .bucket(appProperties.getS3Bucket())
        .key(fileName)
        .build());
  }

  @Override
  public void deleteCvById(Long id) {
    CVFile cvFile = cvFileRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("CV not found"));

    // Delete from S3
    S3Client s3 = createS3Client();
    s3.deleteObject(DeleteObjectRequest.builder()
        .bucket(appProperties.getS3Bucket())
        .key(cvFile.getS3Key())
        .build());

    // Delete from DB
    cvFileRepository.deleteById(id);
  }

  @Override
  public List<CVFile> getCVsForCandidate(Long candidateId) {
    return cvFileRepository.findAllByCandidateId(candidateId);
  }


}
