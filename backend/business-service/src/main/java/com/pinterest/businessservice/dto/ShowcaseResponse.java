package com.pinterest.businessservice.dto;

import java.util.List;
import lombok.Data;

@Data
public class ShowcaseResponse {
  private Long id;
  private Long businessId;
  private String businessName;
  private String title;
  private String theme;
  private String description;
  private String heroImageUrl;
  private List<String> keywords;
}
