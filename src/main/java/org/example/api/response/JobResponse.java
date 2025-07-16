package org.example.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JobResponse {
  private Long id;
  private String title;
  private String description;
  private String department;
  private String location;
}
