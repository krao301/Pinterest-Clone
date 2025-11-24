package com.pinterest.businessservice.api;

import com.pinterest.businessservice.dto.ShowcaseRequest;
import com.pinterest.businessservice.dto.ShowcaseResponse;
import com.pinterest.businessservice.service.ShowcaseService;
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
@RequestMapping("/api/business/showcases")
public class ShowcaseController {

  private final ShowcaseService showcaseService;

  public ShowcaseController(ShowcaseService showcaseService) {
    this.showcaseService = showcaseService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ShowcaseResponse createShowcase(@Valid @RequestBody ShowcaseRequest request) {
    return showcaseService.createShowcase(request);
  }

  @GetMapping
  public List<ShowcaseResponse> listShowcases(
      @RequestParam(value = "businessId", required = false) Long businessId,
      @RequestParam(value = "theme", required = false) String theme) {
    return showcaseService.listShowcases(businessId, theme);
  }

  @GetMapping("/{id}")
  public ShowcaseResponse getShowcase(@PathVariable Long id) {
    return showcaseService.getShowcase(id);
  }
}
