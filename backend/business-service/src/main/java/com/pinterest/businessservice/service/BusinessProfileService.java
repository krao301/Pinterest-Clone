package com.pinterest.businessservice.service;

import com.pinterest.businessservice.domain.BusinessProfile;
import com.pinterest.businessservice.dto.BusinessProfileRequest;
import com.pinterest.businessservice.dto.BusinessProfileResponse;
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
public class BusinessProfileService {

  private final BusinessProfileRepository businessProfileRepository;
  private final ShowcaseRepository showcaseRepository;
  private final ModelMapper modelMapper;

  public BusinessProfileService(BusinessProfileRepository businessProfileRepository,
      ShowcaseRepository showcaseRepository, ModelMapper modelMapper) {
    this.businessProfileRepository = businessProfileRepository;
    this.showcaseRepository = showcaseRepository;
    this.modelMapper = modelMapper;
  }

  public BusinessProfileResponse createProfile(BusinessProfileRequest request) {
    BusinessProfile profile = BusinessProfile.builder()
        .name(request.getName())
        .description(request.getDescription())
        .website(request.getWebsite())
        .logoUrl(request.getLogoUrl())
        .categories(request.getCategories())
        .build();
    BusinessProfile saved = businessProfileRepository.save(profile);
    return toResponse(saved);
  }

  public List<BusinessProfileResponse> listProfiles(String query, String category) {
    List<BusinessProfile> profiles;
    if (query != null && !query.isBlank()) {
      profiles = businessProfileRepository.findByNameContainingIgnoreCase(query.trim());
    } else if (category != null && !category.isBlank()) {
      profiles = businessProfileRepository.findByCategoriesContainingIgnoreCase(category.trim());
    } else {
      profiles = businessProfileRepository.findAll();
    }
    return profiles.stream().map(this::toResponse).collect(Collectors.toList());
  }

  public BusinessProfileResponse getProfile(Long id) {
    BusinessProfile profile = businessProfileRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Business profile not found"));
    return toResponse(profile);
  }

  private BusinessProfileResponse toResponse(BusinessProfile profile) {
    BusinessProfileResponse response = modelMapper.map(profile, BusinessProfileResponse.class);
    int showcaseCount = showcaseRepository.findByBusinessProfileId(profile.getId()).size();
    response.setShowcaseCount(showcaseCount);
    return response;
  }
}
