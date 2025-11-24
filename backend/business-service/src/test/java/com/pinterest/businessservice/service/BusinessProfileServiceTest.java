package com.pinterest.businessservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.pinterest.businessservice.domain.BusinessProfile;
import com.pinterest.businessservice.dto.BusinessProfileRequest;
import com.pinterest.businessservice.dto.BusinessProfileResponse;
import com.pinterest.businessservice.exception.ResourceNotFoundException;
import com.pinterest.businessservice.repository.BusinessProfileRepository;
import com.pinterest.businessservice.repository.ShowcaseRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class BusinessProfileServiceTest {

  @Mock
  private BusinessProfileRepository businessProfileRepository;

  @Mock
  private ShowcaseRepository showcaseRepository;

  @InjectMocks
  private BusinessProfileService businessProfileService;

  @BeforeEach
  void setup() {
    businessProfileService = new BusinessProfileService(businessProfileRepository, showcaseRepository,
        new ModelMapper());
  }

  @Test
  void createProfile_mapsAndPersists() {
    BusinessProfileRequest request = new BusinessProfileRequest();
    request.setName("Brand");
    request.setWebsite("https://brand.test");
    request.setCategories(List.of("home"));

    BusinessProfile saved = BusinessProfile.builder()
        .id(1L)
        .name("Brand")
        .website("https://brand.test")
        .categories(List.of("home"))
        .build();

    when(businessProfileRepository.save(any(BusinessProfile.class))).thenReturn(saved);
    when(showcaseRepository.findByBusinessProfileId(1L)).thenReturn(Collections.emptyList());

    BusinessProfileResponse response = businessProfileService.createProfile(request);

    assertEquals(1L, response.getId());
    assertEquals("Brand", response.getName());
    assertEquals(0, response.getShowcaseCount());
  }

  @Test
  void getProfile_whenMissing_throwsNotFound() {
    when(businessProfileRepository.findById(5L)).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> businessProfileService.getProfile(5L));
  }

  @Test
  void listProfiles_filtersByQuery() {
    BusinessProfile profile = BusinessProfile.builder().id(2L).name("KitchenCo").build();
    when(businessProfileRepository.findByNameContainingIgnoreCase("kit"))
        .thenReturn(List.of(profile));
    when(showcaseRepository.findByBusinessProfileId(2L)).thenReturn(Collections.emptyList());

    List<BusinessProfileResponse> responses = businessProfileService.listProfiles(" kit ", null);
    assertEquals(1, responses.size());
    assertEquals("KitchenCo", responses.get(0).getName());
  }
}
