package com.pinterest.businessservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class SponsoredPinRequest {

  @NotNull(message = "Please provide a valid businessId")
  private Long businessId;

  @NotBlank(message = "Please provide a valid title")
  private String title;

  @NotBlank(message = "Please provide a valid imageUrl")
  private String imageUrl;

  @NotBlank(message = "Please provide a valid destinationUrl")
  private String destinationUrl;

  private String campaignName;
  private String callToAction;
  private List<String> keywords;
}
