package com.pinterest.userservice.exception;

public class CircuitOpenException extends RuntimeException {

  private final long retryAfterSeconds;

  public CircuitOpenException(long retryAfterSeconds) {
    super("Login temporarily disabled. Please wait before retrying.");
    this.retryAfterSeconds = retryAfterSeconds;
  }

  public long getRetryAfterSeconds() {
    return retryAfterSeconds;
  }
}
