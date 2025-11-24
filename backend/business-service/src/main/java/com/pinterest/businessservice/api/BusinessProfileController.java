package com.pinterest.businessservice.api;

import com.pinterest.businessservice.dto.BusinessProfileRequest;
import com.pinterest.businessservice.dto.BusinessProfileResponse;
import com.pinterest.businessservice.service.BusinessProfileService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/business/profiles")
public class BusinessProfileController {

  private final BusinessProfileService businessProfileService;

  public BusinessProfileController(BusinessProfileService businessProfileService) {
    this.businessProfileService = businessProfileService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public BusinessProfileResponse createProfile(@Valid @RequestBody BusinessProfileRequest request) {
    return businessProfileService.createProfile(request);
  }

  @GetMapping
  public List<BusinessProfileResponse> listProfiles(
      @RequestParam(value = "q", required = false) String query,
      @RequestParam(value = "category", required = false) String category) {
    return businessProfileService.listProfiles(query, category);
  }

  @GetMapping("/{id}")
  public BusinessProfileResponse getProfile(@PathVariable Long id) {
    return businessProfileService.getProfile(id);
  }
}
