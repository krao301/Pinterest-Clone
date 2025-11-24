package com.pinterest.contentservice.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Data;

@Data
public class PinRequest {

  @NotBlank(message = "Title is required")
  private String title;

  @NotBlank(message = "Description is required")
  private String description;

  @NotBlank(message = "Media URL is required")
  private String mediaUrl;

  private String sourceUrl;

  private List<String> keywords;

  private Long boardId;

  private boolean draft;

  private boolean visible = true;
}
