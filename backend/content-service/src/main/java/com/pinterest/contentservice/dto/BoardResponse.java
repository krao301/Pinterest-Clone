package com.pinterest.contentservice.dto;

import com.pinterest.contentservice.domain.Visibility;
import java.time.Instant;
import lombok.Data;

@Data
public class BoardResponse {
  private Long id;
  private String name;
  private String description;
  private String coverImageUrl;
  private String ownerUsername;
  private Visibility visibility;
  private int pinCount;
  private Instant createdAt;
}
