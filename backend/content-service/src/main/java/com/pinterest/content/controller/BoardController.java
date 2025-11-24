package com.pinterest.content.controller;

import com.pinterest.content.model.Board;
import com.pinterest.content.repository.BoardRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/boards")
public class BoardController {

    private final BoardRepository boardRepository;

    public BoardController(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @PostMapping
    public ResponseEntity<Board> create(@RequestBody Board board) {
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() != null) {
            try {
                var userId = Long.parseLong(auth.getPrincipal().toString());
                board.setOwnerId(userId);
            } catch (Exception e) {
            }
        }
        var saved = boardRepository.save(board);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Board> get(@PathVariable Long id) {
        return boardRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Board>> list(@RequestParam(required = false) Long ownerId) {
        if (ownerId != null) {
            return ResponseEntity.ok(boardRepository.findByOwnerId(ownerId));
        }
        return ResponseEntity.ok(boardRepository.findAll());
    }
}
