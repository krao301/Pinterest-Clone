package com.pinterest.businessservice.service;

import com.pinterest.businessservice.domain.BusinessProfile;
import com.pinterest.businessservice.domain.SponsoredPin;
import com.pinterest.businessservice.dto.SponsoredPinRequest;
import com.pinterest.businessservice.dto.SponsoredPinResponse;
import com.pinterest.businessservice.exception.ResourceNotFoundException;
import com.pinterest.businessservice.repository.BusinessProfileRepository;
import com.pinterest.businessservice.repository.SponsoredPinRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SponsoredContentService {

  private final SponsoredPinRepository sponsoredPinRepository;
  private final BusinessProfileRepository businessProfileRepository;
  private final ModelMapper modelMapper;

  public SponsoredContentService(SponsoredPinRepository sponsoredPinRepository,
      BusinessProfileRepository businessProfileRepository, ModelMapper modelMapper) {
    this.sponsoredPinRepository = sponsoredPinRepository;
    this.businessProfileRepository = businessProfileRepository;
    this.modelMapper = modelMapper;
  }

  public SponsoredPinResponse createSponsoredPin(SponsoredPinRequest request) {
    BusinessProfile profile = businessProfileRepository.findById(request.getBusinessId())
        .orElseThrow(() -> new ResourceNotFoundException("Business profile not found"));

    SponsoredPin sponsoredPin = SponsoredPin.builder()
        .title(request.getTitle())
        .imageUrl(request.getImageUrl())
        .destinationUrl(request.getDestinationUrl())
        .campaignName(request.getCampaignName())
        .callToAction(request.getCallToAction())
        .keywords(request.getKeywords())
        .active(true)
        .businessProfile(profile)
        .build();

    SponsoredPin saved = sponsoredPinRepository.save(sponsoredPin);
    return toResponse(saved);
  }

  public List<SponsoredPinResponse> listSponsoredPins(Long businessId, String keyword) {
    List<SponsoredPin> pins;
    if (businessId != null) {
      pins = sponsoredPinRepository.findByBusinessProfileId(businessId);
    } else if (keyword != null && !keyword.isBlank()) {
      pins = sponsoredPinRepository.findByKeywordsContainingIgnoreCase(keyword.trim());
    } else {
      pins = sponsoredPinRepository.findAll();
    }
    return pins.stream().map(this::toResponse).collect(Collectors.toList());
  }

  public SponsoredPinResponse getSponsoredPin(Long id) {
    SponsoredPin sponsoredPin = sponsoredPinRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Sponsored pin not found"));
    return toResponse(sponsoredPin);
  }

  private SponsoredPinResponse toResponse(SponsoredPin sponsoredPin) {
    SponsoredPinResponse response = modelMapper.map(sponsoredPin, SponsoredPinResponse.class);
    BusinessProfile profile = sponsoredPin.getBusinessProfile();
    response.setBusinessId(profile != null ? profile.getId() : null);
    response.setBusinessName(profile != null ? profile.getName() : null);
    return response;
  }
}
