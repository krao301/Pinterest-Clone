package com.pinterest.contentservice.api;

import java.time.Instant;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

  @GetMapping("/api/content/ping")
  public Map<String, Object> ping() {
    return Map.of(
        "service", "content-service",
        "status", "ok",
        "timestamp", Instant.now().toString());
  }
}
