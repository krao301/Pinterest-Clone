package com.pinterest.contentservice.dto;

import com.pinterest.contentservice.domain.Visibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BoardRequest {

  @NotBlank(message = "Board name is required")
  private String name;

  private String description;

  private String coverImageUrl;

  private String ownerUsername;

  @NotNull(message = "Visibility is required")
  private Visibility visibility;
}
