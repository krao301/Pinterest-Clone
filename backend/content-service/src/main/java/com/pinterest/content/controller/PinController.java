package com.pinterest.content.controller;

import com.pinterest.content.model.Pin;
import com.pinterest.content.repository.PinRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.pinterest.content.service.StorageService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pins")
public class PinController {

    private final PinRepository pinRepository;

    private final StorageService storageService;

    public PinController(PinRepository pinRepository, StorageService storageService) {
        this.pinRepository = pinRepository;
        this.storageService = storageService;
    }

    @PostMapping
    public ResponseEntity<Pin> create(@RequestBody Pin pin) {
        // require authenticated user
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return ResponseEntity.status(401).build();
        }
        try {
            var userId = Long.parseLong(auth.getPrincipal().toString());
            pin.setCreatorId(userId);
        } catch (Exception e) {
            // ignore, proceed without setting creator
        }
        var saved = pinRepository.save(pin);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) {
        try {
            var url = storageService.store(file);
            return ResponseEntity.ok(Map.of("success", true, "data", Map.of("imageUrl", url)));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Pin>> list() {
        return ResponseEntity.ok(pinRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pin> get(@PathVariable Long id) {
        return pinRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
