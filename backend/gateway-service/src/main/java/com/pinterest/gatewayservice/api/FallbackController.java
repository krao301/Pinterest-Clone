package com.pinterest.gatewayservice.api;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gateway")
public class FallbackController {

  @GetMapping("/fallback")
  public ResponseEntity<Map<String, Object>> fallback() {
    return ResponseEntity.ok(Map.of(
        "message", "Gateway circuit is open. Please retry shortly.",
        "status", 503));
  }
}

