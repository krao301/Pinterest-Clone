package com.pinterest.content.controller;

import com.pinterest.content.model.Pin;
import com.pinterest.content.model.Board;
import com.pinterest.content.repository.PinRepository;
import com.pinterest.content.repository.BoardRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
public class SearchController {

    private final PinRepository pinRepository;
    private final BoardRepository boardRepository;

    public SearchController(PinRepository pinRepository, BoardRepository boardRepository) {
        this.pinRepository = pinRepository;
        this.boardRepository = boardRepository;
    }

    @GetMapping
    public ResponseEntity<?> search(@RequestParam String q) {
        // Very simple search implementation using contains on title/description
        List<Pin> pins = pinRepository.findAll().stream()
                .filter(p -> (p.getTitle() != null && p.getTitle().toLowerCase().contains(q.toLowerCase()))
                        || (p.getDescription() != null && p.getDescription().toLowerCase().contains(q.toLowerCase())))
                .toList();

        List<Board> boards = boardRepository.findAll().stream()
                .filter(b -> (b.getTitle() != null && b.getTitle().toLowerCase().contains(q.toLowerCase()))
                        || (b.getDescription() != null && b.getDescription().toLowerCase().contains(q.toLowerCase())))
                .toList();

        return ResponseEntity.ok(new java.util.HashMap<>() {{ put("pins", pins); put("boards", boards); }});
    }
}
