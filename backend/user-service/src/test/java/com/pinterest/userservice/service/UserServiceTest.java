package com.pinterest.userservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.pinterest.userservice.domain.User;
import com.pinterest.userservice.dto.RegisterRequest;
import com.pinterest.userservice.dto.UserResponse;
import com.pinterest.userservice.exception.DuplicateResourceException;
import com.pinterest.userservice.exception.InvalidCredentialsException;
import com.pinterest.userservice.repository.UserRepository;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  private PasswordEncoder passwordEncoder;
  private UserService userService;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
    passwordEncoder = new BCryptPasswordEncoder();
    userService = new UserService(userRepository, passwordEncoder, new ModelMapper());
  }

  @Test
  void register_shouldCreateUserWhenEmailAndUsernameAreUnique() {
    RegisterRequest request = new RegisterRequest();
    request.setEmail("test@example.com");
    request.setUsername("testuser");
    request.setPassword("Password1!");
    request.setConfirmPassword("Password1!");

    when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
    when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
    when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
      User user = invocation.getArgument(0);
      user.setId(1L);
      return user;
    });

    UserResponse response = userService.register(request);

    assertThat(response.getId()).isEqualTo(1L);
    assertThat(response.getEmail()).isEqualTo(request.getEmail());
    assertThat(response.getUsername()).isEqualTo(request.getUsername());
  }

  @Test
  void register_shouldRejectDuplicateEmail() {
    RegisterRequest request = new RegisterRequest();
    request.setEmail("test@example.com");
    request.setUsername("another");
    request.setPassword("Password1!");
    request.setConfirmPassword("Password1!");

    when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

    assertThatThrownBy(() -> userService.register(request))
        .isInstanceOf(DuplicateResourceException.class)
        .hasMessageContaining("Email is already in use");
  }

  @Test
  void authenticate_shouldReturnUserWhenPasswordMatches() {
    User user = User.builder()
        .id(5L)
        .email("login@example.com")
        .username("loginuser")
        .passwordHash(passwordEncoder.encode("Password1!"))
        .createdAt(Instant.now())
        .build();

    when(userRepository.findByEmail("login@example.com")).thenReturn(Optional.of(user));

    UserResponse response = userService.authenticate("login@example.com", "Password1!");

    assertThat(response.getId()).isEqualTo(5L);
    assertThat(response.getUsername()).isEqualTo("loginuser");
  }

  @Test
  void authenticate_shouldRejectInvalidPassword() {
    User user = User.builder()
        .id(2L)
        .email("wrong@example.com")
        .username("wronguser")
        .passwordHash(passwordEncoder.encode("Password1!"))
        .createdAt(Instant.now())
        .build();

    when(userRepository.findByEmail("wrong@example.com")).thenReturn(Optional.of(user));

    assertThatThrownBy(() -> userService.authenticate("wrong@example.com", "BadPass1!"))
        .isInstanceOf(InvalidCredentialsException.class);
  }
}
