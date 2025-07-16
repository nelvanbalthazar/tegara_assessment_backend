package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.config.AppProperties;
import org.example.entity.CVFile;
import org.example.entity.Candidate;
import org.example.repository.CVFileRepository;
import org.example.repository.CandidateRepository;
import org.example.service.UploadService;
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
      String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
      S3Client s3 = createS3Client();

      // Upload to S3
      s3.putObject(
          PutObjectRequest.builder()
              .bucket(appProperties.getS3Bucket())
              .key(fileName)
              .build(),
          RequestBody.fromBytes(file.getBytes())
      );

      // Textract client
      TextractClient textract = TextractClient.builder()
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

      Document document = Document.builder()
          .s3Object(software.amazon.awssdk.services.textract.model.S3Object.builder()
              .bucket(appProperties.getS3Bucket())
              .name(fileName)
              .build())
          .build();

      AnalyzeDocumentRequest request = AnalyzeDocumentRequest.builder()
          .document(document)
          .featureTypes(FeatureType.FORMS, FeatureType.TABLES)
          .build();

      AnalyzeDocumentResponse response = textract.analyzeDocument(request);

      String extractedText = response.blocks().stream()
          .filter(block -> block.blockType().equals(BlockType.LINE))
          .map(Block::text)
          .collect(Collectors.joining("\n"));

      // Save to DB
      Candidate candidate = candidateRepository.findById(candidateId)
          .orElseThrow(() -> new RuntimeException("Candidate not found"));

      CVFile cvFile = CVFile.builder()
          .fileName(fileName)
          .s3Key(fileName)
          .extractedText(extractedText)
          .uploadedAt(LocalDateTime.now())
          .candidate(candidate)
          .build();

      CVFile saved = cvFileRepository.save(cvFile);
      return saved;

    } catch (IOException e) {
      throw new RuntimeException("File read error", e);
    } catch (S3Exception | TextractException e) {
      throw new RuntimeException("AWS error: " + e.awsErrorDetails().errorMessage(), e);
    }
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
