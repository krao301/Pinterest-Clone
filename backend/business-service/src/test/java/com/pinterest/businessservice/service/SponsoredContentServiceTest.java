package com.pinterest.businessservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.pinterest.businessservice.domain.BusinessProfile;
import com.pinterest.businessservice.domain.SponsoredPin;
import com.pinterest.businessservice.dto.SponsoredPinRequest;
import com.pinterest.businessservice.dto.SponsoredPinResponse;
import com.pinterest.businessservice.exception.ResourceNotFoundException;
import com.pinterest.businessservice.repository.BusinessProfileRepository;
import com.pinterest.businessservice.repository.SponsoredPinRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class SponsoredContentServiceTest {

  @Mock
  private SponsoredPinRepository sponsoredPinRepository;

  @Mock
  private BusinessProfileRepository businessProfileRepository;

  private SponsoredContentService sponsoredContentService;

  @BeforeEach
  void setup() {
    sponsoredContentService = new SponsoredContentService(sponsoredPinRepository, businessProfileRepository,
        new ModelMapper());
  }

  @Test
  void createSponsoredPin_requiresExistingProfile() {
    SponsoredPinRequest request = new SponsoredPinRequest();
    request.setBusinessId(2L);
    request.setTitle("Ad");
    request.setImageUrl("https://img.test/ad.png");
    request.setDestinationUrl("https://dest.test");

    BusinessProfile profile = BusinessProfile.builder().id(2L).name("Brand").build();
    SponsoredPin saved = SponsoredPin.builder().id(7L).title("Ad").businessProfile(profile).build();

    when(businessProfileRepository.findById(2L)).thenReturn(Optional.of(profile));
    when(sponsoredPinRepository.save(any(SponsoredPin.class))).thenReturn(saved);

    SponsoredPinResponse response = sponsoredContentService.createSponsoredPin(request);
    assertEquals(7L, response.getId());
    assertEquals("Brand", response.getBusinessName());
  }

  @Test
  void getSponsoredPin_whenMissing_throws() {
    when(sponsoredPinRepository.findById(9L)).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> sponsoredContentService.getSponsoredPin(9L));
  }

  @Test
  void listSponsoredPins_filtersByKeyword() {
    SponsoredPin pin = SponsoredPin.builder().id(4L).title("Summer").build();
    when(sponsoredPinRepository.findByKeywordsContainingIgnoreCase("summer"))
        .thenReturn(List.of(pin));

    List<SponsoredPinResponse> responses = sponsoredContentService.listSponsoredPins(null, "summer");
    assertEquals(1, responses.size());
    assertEquals("Summer", responses.get(0).getTitle());
  }
}
