package org.example.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {

  // === General Application Settings ===
  private String name;
  private String uploadDir;
  private String frontendUrl;

  // === AWS S3 Settings ===
  private String s3Bucket;
  private String s3Region;

  // === AWS Textract ===
  private String textractRegion;

  // === AWS Credentials (used if not on EC2 IAM Role) ===
  private String accessKey;
  private String secretKey;
}
