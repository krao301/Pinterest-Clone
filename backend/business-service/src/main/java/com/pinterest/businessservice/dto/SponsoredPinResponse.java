package com.pinterest.businessservice.dto;

import java.util.List;
import lombok.Data;

@Data
public class SponsoredPinResponse {
  private Long id;
  private Long businessId;
  private String businessName;
  private String title;
  private String imageUrl;
  private String destinationUrl;
  private String campaignName;
  private String callToAction;
  private boolean active;
  private List<String> keywords;
}
