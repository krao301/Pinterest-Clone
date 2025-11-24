package com.pinterest.userservice.dto;

import java.time.Instant;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserResponse {
  Long id;
  String email;
  String username;
  Instant createdAt;
}
