package com.pinterest.contentservice.dto;

import java.time.Instant;
import java.util.List;
import lombok.Data;

@Data
public class PinResponse {
  private Long id;
  private String title;
  private String description;
  private String mediaUrl;
  private String sourceUrl;
  private List<String> keywords;
  private boolean draft;
  private boolean visible;
  private Instant createdAt;
  private Long boardId;
  private String boardName;
}
