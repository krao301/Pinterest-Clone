package com.pinterest.businessservice.dto;

import java.util.List;
import lombok.Data;

@Data
public class BusinessProfileResponse {
  private Long id;
  private String name;
  private String description;
  private String website;
  private String logoUrl;
  private List<String> categories;
  private int showcaseCount;
}
