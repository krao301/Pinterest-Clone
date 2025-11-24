package com.pinterest.contentservice.service;

import com.pinterest.contentservice.domain.Board;
import com.pinterest.contentservice.domain.Pin;
import com.pinterest.contentservice.dto.PinRequest;
import com.pinterest.contentservice.dto.PinResponse;
import com.pinterest.contentservice.exception.ResourceNotFoundException;
import com.pinterest.contentservice.repository.BoardRepository;
import com.pinterest.contentservice.repository.PinRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PinService {

  private final PinRepository pinRepository;
  private final BoardRepository boardRepository;
  private final ModelMapper modelMapper;

  public PinService(PinRepository pinRepository, BoardRepository boardRepository, ModelMapper modelMapper) {
    this.pinRepository = pinRepository;
    this.boardRepository = boardRepository;
    this.modelMapper = modelMapper;
  }

  public PinResponse createPin(PinRequest request) {
    Board board = null;
    if (request.getBoardId() != null) {
      board = boardRepository.findById(request.getBoardId())
          .orElseThrow(() -> new ResourceNotFoundException("Board not found"));
    }

    Pin pin = Pin.builder()
        .title(request.getTitle())
        .description(request.getDescription())
        .mediaUrl(request.getMediaUrl())
        .sourceUrl(request.getSourceUrl())
        .keywords(joinKeywords(request.getKeywords()))
        .draft(request.isDraft())
        .visible(request.isVisible())
        .board(board)
        .build();

    Pin saved = pinRepository.save(pin);
    return toResponse(saved);
  }

  public List<PinResponse> listPins(String query, Long boardId) {
    List<Pin> pins;
    if (boardId != null) {
      pins = pinRepository.findByBoardId(boardId);
    } else {
      pins = pinRepository.searchPins(query);
    }
    return pins.stream().map(this::toResponse).collect(Collectors.toList());
  }

  public PinResponse getPin(Long id) {
    Pin pin = pinRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Pin not found"));
    return toResponse(pin);
  }

  public void deletePin(Long id) {
    Pin pin = pinRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Pin not found"));
    pinRepository.delete(pin);
  }

  private PinResponse toResponse(Pin pin) {
    PinResponse response = modelMapper.map(pin, PinResponse.class);
    response.setKeywords(splitKeywords(pin.getKeywords()));
    if (pin.getBoard() != null) {
      response.setBoardId(pin.getBoard().getId());
      response.setBoardName(pin.getBoard().getName());
    }
    return response;
  }

  private String joinKeywords(List<String> keywords) {
    if (keywords == null || keywords.isEmpty()) {
      return null;
    }
    return keywords.stream()
        .map(String::trim)
        .filter(s -> !s.isEmpty())
        .collect(Collectors.joining(","));
  }

  private List<String> splitKeywords(String keywords) {
    if (keywords == null || keywords.isBlank()) {
      return Collections.emptyList();
    }
    return Arrays.stream(keywords.split(","))
        .map(String::trim)
        .filter(s -> !s.isEmpty())
        .collect(Collectors.toList());
  }
}
