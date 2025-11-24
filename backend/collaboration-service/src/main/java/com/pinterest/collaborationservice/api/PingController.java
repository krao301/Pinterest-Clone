package com.pinterest.collaborationservice.api;

import java.time.Instant;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

  @GetMapping("/api/collaboration/ping")
  public Map<String, Object> ping() {
    return Map.of(
        "service", "collaboration-service",
        "status", "ok",
        "timestamp", Instant.now().toString());
  }
}
