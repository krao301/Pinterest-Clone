package com.pinterest.userservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.pinterest.userservice.dto.UserResponse;
import com.pinterest.userservice.exception.CircuitOpenException;
import com.pinterest.userservice.exception.InvalidCredentialsException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoginThrottleServiceTest {

  private LoginThrottleService loginThrottleService;
  private CircuitBreaker circuitBreaker;

  @BeforeEach
  void setUp() {
    CircuitBreakerConfig config = CircuitBreakerConfig.custom()
        .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.TIME_BASED)
        .slidingWindowSize(30)
        .failureRateThreshold(50)
        .minimumNumberOfCalls(3)
        .waitDurationInOpenState(Duration.ofSeconds(60))
        .permittedNumberOfCallsInHalfOpenState(1)
        .automaticTransitionFromOpenToHalfOpenEnabled(true)
        .build();
    CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
    this.loginThrottleService = new LoginThrottleService(registry);
    this.circuitBreaker = registry.circuitBreaker("loginCircuit");
  }

  @Test
  void allowsSuccessfulLoginWhenCircuitClosed() {
    UserResponse response = loginThrottleService.attemptLogin(() -> UserResponse.builder()
        .id(1L)
        .email("test@example.com")
        .username("tester")
        .build());

    assertNotNull(response);
    assertEquals(CircuitBreaker.State.CLOSED, circuitBreaker.getState());
  }

  @Test
  void opensCircuitAfterThreeFailedLogins() {
    for (int attempt = 0; attempt < 3; attempt++) {
      assertThrows(InvalidCredentialsException.class, () -> loginThrottleService.attemptLogin(() -> {
        throw new InvalidCredentialsException("Invalid email or password");
      }));
    }

    assertEquals(CircuitBreaker.State.OPEN, circuitBreaker.getState());
    assertThrows(CircuitOpenException.class, () -> loginThrottleService.attemptLogin(() -> UserResponse.builder().build()));
  }
}
