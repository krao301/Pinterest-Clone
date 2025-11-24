package com.pinterest.businessservice.api;

import com.pinterest.businessservice.dto.SponsoredPinRequest;
import com.pinterest.businessservice.dto.SponsoredPinResponse;
import com.pinterest.businessservice.service.SponsoredContentService;
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
@RequestMapping("/api/business/sponsored-pins")
public class SponsoredPinController {

  private final SponsoredContentService sponsoredContentService;

  public SponsoredPinController(SponsoredContentService sponsoredContentService) {
    this.sponsoredContentService = sponsoredContentService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public SponsoredPinResponse createSponsoredPin(@Valid @RequestBody SponsoredPinRequest request) {
    return sponsoredContentService.createSponsoredPin(request);
  }

  @GetMapping
  public List<SponsoredPinResponse> listSponsoredPins(
      @RequestParam(value = "businessId", required = false) Long businessId,
      @RequestParam(value = "keyword", required = false) String keyword) {
    return sponsoredContentService.listSponsoredPins(businessId, keyword);
  }

  @GetMapping("/{id}")
  public SponsoredPinResponse getSponsoredPin(@PathVariable Long id) {
    return sponsoredContentService.getSponsoredPin(id);
  }
}
