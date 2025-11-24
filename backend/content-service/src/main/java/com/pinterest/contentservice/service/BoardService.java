package com.pinterest.contentservice.service;

import com.pinterest.contentservice.domain.Board;
import com.pinterest.contentservice.domain.Visibility;
import com.pinterest.contentservice.dto.BoardRequest;
import com.pinterest.contentservice.dto.BoardResponse;
import com.pinterest.contentservice.exception.ResourceNotFoundException;
import com.pinterest.contentservice.repository.BoardRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BoardService {

  private final BoardRepository boardRepository;
  private final ModelMapper modelMapper;

  public BoardService(BoardRepository boardRepository, ModelMapper modelMapper) {
    this.boardRepository = boardRepository;
    this.modelMapper = modelMapper;
  }

  public BoardResponse createBoard(BoardRequest request) {
    Board board = Board.builder()
        .name(request.getName())
        .description(request.getDescription())
        .coverImageUrl(request.getCoverImageUrl())
        .ownerUsername(request.getOwnerUsername())
        .visibility(request.getVisibility())
        .build();
    Board saved = boardRepository.save(board);
    return toResponse(saved);
  }

  public List<BoardResponse> listBoards(String query, Visibility visibility) {
    List<Board> boards;
    if (query != null && !query.isBlank()) {
      boards = boardRepository.findByNameContainingIgnoreCase(query.trim());
    } else if (visibility != null) {
      boards = boardRepository.findByVisibility(visibility);
    } else {
      boards = boardRepository.findAll();
    }
    return boards.stream().map(this::toResponse).collect(Collectors.toList());
  }

  public BoardResponse getBoard(Long id) {
    Board board = boardRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Board not found"));
    return toResponse(board);
  }

  private BoardResponse toResponse(Board board) {
    BoardResponse response = modelMapper.map(board, BoardResponse.class);
    response.setPinCount(board.getPins() != null ? board.getPins().size() : 0);
    return response;
  }
}
