package com.pinterest.contentservice.api;

import com.pinterest.contentservice.domain.Visibility;
import com.pinterest.contentservice.dto.BoardRequest;
import com.pinterest.contentservice.dto.BoardResponse;
import com.pinterest.contentservice.service.BoardService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

  private final BoardService boardService;

  public BoardController(BoardService boardService) {
    this.boardService = boardService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public BoardResponse createBoard(@Valid @RequestBody BoardRequest request) {
    return boardService.createBoard(request);
  }

  @GetMapping
  public List<BoardResponse> listBoards(
      @RequestParam(value = "q", required = false) String query,
      @RequestParam(value = "visibility", required = false) Visibility visibility) {
    return boardService.listBoards(query, visibility);
  }

  @GetMapping("/{id}")
  public BoardResponse getBoard(@PathVariable Long id) {
    return boardService.getBoard(id);
  }
}
