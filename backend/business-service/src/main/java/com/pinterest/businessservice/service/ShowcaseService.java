package com.pinterest.businessservice.service;

import com.pinterest.businessservice.domain.BusinessProfile;
import com.pinterest.businessservice.domain.Showcase;
import com.pinterest.businessservice.dto.ShowcaseRequest;
import com.pinterest.businessservice.dto.ShowcaseResponse;
import com.pinterest.businessservice.exception.ResourceNotFoundException;
import com.pinterest.businessservice.repository.BusinessProfileRepository;
import com.pinterest.businessservice.repository.ShowcaseRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ShowcaseService {

  private final ShowcaseRepository showcaseRepository;
  private final BusinessProfileRepository businessProfileRepository;
  private final ModelMapper modelMapper;

  public ShowcaseService(ShowcaseRepository showcaseRepository,
      BusinessProfileRepository businessProfileRepository, ModelMapper modelMapper) {
    this.showcaseRepository = showcaseRepository;
    this.businessProfileRepository = businessProfileRepository;
    this.modelMapper = modelMapper;
  }

  public ShowcaseResponse createShowcase(ShowcaseRequest request) {
    BusinessProfile businessProfile = businessProfileRepository.findById(request.getBusinessId())
        .orElseThrow(() -> new ResourceNotFoundException("Business profile not found"));

    Showcase showcase = Showcase.builder()
        .title(request.getTitle())
        .theme(request.getTheme())
        .description(request.getDescription())
        .heroImageUrl(request.getHeroImageUrl())
        .keywords(request.getKeywords())
        .businessProfile(businessProfile)
        .build();

    Showcase saved = showcaseRepository.save(showcase);
    return toResponse(saved);
  }

  public List<ShowcaseResponse> listShowcases(Long businessId, String theme) {
    List<Showcase> showcases;
    if (businessId != null) {
      showcases = showcaseRepository.findByBusinessProfileId(businessId);
    } else if (theme != null && !theme.isBlank()) {
      showcases = showcaseRepository.findByThemeContainingIgnoreCase(theme.trim());
    } else {
      showcases = showcaseRepository.findAll();
    }
    return showcases.stream().map(this::toResponse).collect(Collectors.toList());
  }

  public ShowcaseResponse getShowcase(Long id) {
    Showcase showcase = showcaseRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Showcase not found"));
    return toResponse(showcase);
  }

  private ShowcaseResponse toResponse(Showcase showcase) {
    ShowcaseResponse response = modelMapper.map(showcase, ShowcaseResponse.class);
    BusinessProfile profile = showcase.getBusinessProfile();
    response.setBusinessId(profile != null ? profile.getId() : null);
    response.setBusinessName(profile != null ? profile.getName() : null);
    return response;
  }
}
