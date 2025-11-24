package com.pinterest.businessservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;

@Data
public class BusinessProfileRequest {

  @NotBlank(message = "Please provide a valid name")
  private String name;

  @Size(max = 500, message = "Description must be under 500 characters")
  private String description;

  @NotBlank(message = "Please provide a valid website")
  private String website;

  private String logoUrl;
  private List<String> categories;
}
