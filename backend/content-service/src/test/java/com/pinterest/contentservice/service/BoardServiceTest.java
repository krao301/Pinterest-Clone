package com.pinterest.contentservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.pinterest.contentservice.domain.Board;
import com.pinterest.contentservice.domain.Visibility;
import com.pinterest.contentservice.dto.BoardRequest;
import com.pinterest.contentservice.dto.BoardResponse;
import com.pinterest.contentservice.exception.ResourceNotFoundException;
import com.pinterest.contentservice.repository.BoardRepository;
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
class BoardServiceTest {

  @Mock
  private BoardRepository boardRepository;

  private ModelMapper modelMapper;

  @InjectMocks
  private BoardService boardService;

  @BeforeEach
  void setUp() {
    modelMapper = new ModelMapper();
    boardService = new BoardService(boardRepository, modelMapper);
  }

  @Test
  void createBoardPersistsAndReturnsResponse() {
    BoardRequest request = new BoardRequest();
    request.setName("Recipes");
    request.setDescription("Weeknight favorites");
    request.setVisibility(Visibility.PUBLIC);

    Board saved = Board.builder()
        .id(1L)
        .name("Recipes")
        .description("Weeknight favorites")
        .visibility(Visibility.PUBLIC)
        .build();

    when(boardRepository.save(org.mockito.ArgumentMatchers.any(Board.class))).thenReturn(saved);

    BoardResponse response = boardService.createBoard(request);
    assertThat(response.getId()).isEqualTo(1L);
    assertThat(response.getName()).isEqualTo("Recipes");
  }

  @Test
  void listBoardsFiltersByQuery() {
    Board board = Board.builder().id(4L).name("Travel").visibility(Visibility.PUBLIC).build();
    when(boardRepository.findByNameContainingIgnoreCase("tra")).thenReturn(List.of(board));

    List<BoardResponse> responses = boardService.listBoards("tra", null);

    assertThat(responses).hasSize(1);
    assertThat(responses.get(0).getName()).isEqualTo("Travel");
  }

  @Test
  void getBoardThrowsWhenMissing() {
    when(boardRepository.findById(77L)).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> boardService.getBoard(77L));
  }
}
