package com.pinterest.userservice.service;

import com.pinterest.userservice.dto.UserResponse;
import com.pinterest.userservice.exception.CircuitOpenException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.function.Supplier;
import org.springframework.stereotype.Service;

@Service
public class LoginThrottleService {

  private final CircuitBreaker circuitBreaker;
  private final Duration openWaitDuration;
  private volatile Instant lastOpenedAt;

  public LoginThrottleService(CircuitBreakerRegistry circuitBreakerRegistry) {
    this.circuitBreaker = circuitBreakerRegistry.circuitBreaker("loginCircuit");
    this.openWaitDuration = circuitBreaker.getCircuitBreakerConfig().getWaitDurationInOpenState();
    this.circuitBreaker.getEventPublisher().onStateTransition(event -> {
      if (event.getStateTransition().getToState() == CircuitBreaker.State.OPEN) {
        lastOpenedAt = Instant.now();
      }
    });
  }

  public UserResponse attemptLogin(Supplier<UserResponse> loginSupplier) {
    if (circuitBreaker.getState() == CircuitBreaker.State.OPEN) {
      throw new CircuitOpenException(remainingOpenSeconds());
    }
    try {
      return circuitBreaker.executeSupplier(loginSupplier::get);
    } catch (CallNotPermittedException exception) {
      throw new CircuitOpenException(remainingOpenSeconds());
    }
  }

  private long remainingOpenSeconds() {
    Instant openedAt = lastOpenedAt;
    if (Objects.isNull(openedAt)) {
      return openWaitDuration.getSeconds();
    }
    Duration elapsed = Duration.between(openedAt, Instant.now());
    Duration remaining = openWaitDuration.minus(elapsed);
    return Math.max(remaining.getSeconds(), 0);
  }
}
