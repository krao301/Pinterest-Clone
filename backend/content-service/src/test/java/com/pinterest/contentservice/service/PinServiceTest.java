package com.pinterest.contentservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import com.pinterest.contentservice.domain.Board;
import com.pinterest.contentservice.domain.Pin;
import com.pinterest.contentservice.dto.PinRequest;
import com.pinterest.contentservice.dto.PinResponse;
import com.pinterest.contentservice.exception.ResourceNotFoundException;
import com.pinterest.contentservice.repository.BoardRepository;
import com.pinterest.contentservice.repository.PinRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PinServiceTest {

  @Mock
  private PinRepository pinRepository;

  @Mock
  private BoardRepository boardRepository;

  private ModelMapper modelMapper;

  @InjectMocks
  private PinService pinService;

  @BeforeEach
  void setUp() {
    modelMapper = new ModelMapper();
    pinService = new PinService(pinRepository, boardRepository, modelMapper);
  }

  @Test
  void createPinAttachesBoardAndKeywords() {
    PinRequest request = new PinRequest();
    request.setTitle("Trip");
    request.setDescription("Places to visit");
    request.setMediaUrl("https://example.com/img.png");
    request.setKeywords(List.of("travel", "adventure"));
    request.setBoardId(99L);

    Board board = Board.builder().id(99L).name("Europe").build();
    when(boardRepository.findById(99L)).thenReturn(Optional.of(board));

    Pin saved = Pin.builder()
        .id(1L)
        .title(request.getTitle())
        .description(request.getDescription())
        .mediaUrl(request.getMediaUrl())
        .keywords("travel,adventure")
        .board(board)
        .draft(false)
        .visible(true)
        .build();

    when(pinRepository.save(org.mockito.ArgumentMatchers.any(Pin.class))).thenReturn(saved);

    PinResponse response = pinService.createPin(request);

    assertThat(response.getId()).isEqualTo(1L);
    assertThat(response.getBoardId()).isEqualTo(99L);
    assertThat(response.getKeywords()).containsExactly("travel", "adventure");
  }

  @Test
  void createPinThrowsWhenBoardMissing() {
    PinRequest request = new PinRequest();
    request.setTitle("Trip");
    request.setDescription("Places to visit");
    request.setMediaUrl("https://example.com/img.png");
    request.setBoardId(123L);

    when(boardRepository.findById(123L)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> pinService.createPin(request));
  }

  @Test
  void listPinsDelegatesToSearch() {
    Pin pin = Pin.builder().id(5L).title("Art Deco").description("Style").mediaUrl("url")
        .keywords("art,deco").draft(false).visible(true).build();
    when(pinRepository.searchPins("art")).thenReturn(List.of(pin));

    List<PinResponse> results = pinService.listPins("art", null);

    assertThat(results).hasSize(1);
    assertThat(results.get(0).getTitle()).isEqualTo("Art Deco");
    verify(pinRepository).searchPins("art");
  }
}
