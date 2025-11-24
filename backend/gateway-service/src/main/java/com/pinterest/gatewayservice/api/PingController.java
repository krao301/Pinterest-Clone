package com.pinterest.gatewayservice.api;

import java.time.Instant;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

  @GetMapping("/api/gateway/ping")
  public Map<String, Object> ping() {
    return Map.of(
        "service", "gateway-service",
        "status", "ok",
        "timestamp", Instant.now().toString());
  }

  @GetMapping("/api/gateway/fallback")
  public Map<String, Object> fallback() {
    return Map.of(
        "service", "gateway-service",
        "status", "fallback",
        "message", "Service temporarily unavailable",
        "timestamp", Instant.now().toString());
  }
}
