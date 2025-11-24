package com.pinterest.userservice.api;

import com.pinterest.userservice.dto.LoginRequest;
import com.pinterest.userservice.dto.RegisterRequest;
import com.pinterest.userservice.dto.UserResponse;
import com.pinterest.userservice.service.LoginThrottleService;
import com.pinterest.userservice.service.UserService;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final LoginThrottleService loginThrottleService;

  @PostMapping("/register")
  public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
    UserResponse response = userService.register(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/login")
  public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
    UserResponse response = loginThrottleService.attemptLogin(
        () -> userService.authenticate(request.getEmail(), request.getPassword()));
    return ResponseEntity.ok(Map.of(
        "message", "Login successful",
        "user", response));
  }
}
