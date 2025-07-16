package org.example;

import org.example.config.AppProperties;
import org.example.security.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({JwtProperties.class, AppProperties.class})
public class CvPortalApplication {
  public static void main(String[] args) {
    SpringApplication.run(CvPortalApplication.class, args);
  }
}
