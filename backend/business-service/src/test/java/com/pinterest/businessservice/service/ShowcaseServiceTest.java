package com.pinterest.businessservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.pinterest.businessservice.domain.BusinessProfile;
import com.pinterest.businessservice.domain.Showcase;
import com.pinterest.businessservice.dto.ShowcaseRequest;
import com.pinterest.businessservice.dto.ShowcaseResponse;
import com.pinterest.businessservice.exception.ResourceNotFoundException;
import com.pinterest.businessservice.repository.BusinessProfileRepository;
import com.pinterest.businessservice.repository.ShowcaseRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class ShowcaseServiceTest {

  @Mock
  private ShowcaseRepository showcaseRepository;

  @Mock
  private BusinessProfileRepository businessProfileRepository;

  private ShowcaseService showcaseService;

  @BeforeEach
  void setup() {
    showcaseService = new ShowcaseService(showcaseRepository, businessProfileRepository, new ModelMapper());
  }

  @Test
  void createShowcase_linksToBusinessProfile() {
    ShowcaseRequest request = new ShowcaseRequest();
    request.setBusinessId(10L);
    request.setTitle("Holiday Picks");

    BusinessProfile profile = BusinessProfile.builder().id(10L).name("Brand").build();
    Showcase saved = Showcase.builder().id(3L).title("Holiday Picks").businessProfile(profile).build();

    when(businessProfileRepository.findById(10L)).thenReturn(Optional.of(profile));
    when(showcaseRepository.save(any(Showcase.class))).thenReturn(saved);

    ShowcaseResponse response = showcaseService.createShowcase(request);

    assertEquals(3L, response.getId());
    assertEquals(10L, response.getBusinessId());
    assertEquals("Brand", response.getBusinessName());
  }

  @Test
  void getShowcase_notFound_throws() {
    when(showcaseRepository.findById(8L)).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> showcaseService.getShowcase(8L));
  }

  @Test
  void listShowcases_allWhenNoFilters() {
    BusinessProfile profile = BusinessProfile.builder().id(1L).name("Brand").build();
    Showcase showcase = Showcase.builder().id(11L).title("New Arrivals").businessProfile(profile).build();
    when(showcaseRepository.findAll()).thenReturn(List.of(showcase));

    List<ShowcaseResponse> responses = showcaseService.listShowcases(null, null);
    assertEquals(1, responses.size());
    assertEquals("New Arrivals", responses.get(0).getTitle());
  }
}
