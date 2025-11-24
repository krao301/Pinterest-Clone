package com.pinterest.businessservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class ShowcaseRequest {

  @NotNull(message = "Please provide a valid businessId")
  private Long businessId;

  @NotBlank(message = "Please provide a valid title")
  private String title;

  private String theme;
  private String description;
  private String heroImageUrl;
  private List<String> keywords;
}
