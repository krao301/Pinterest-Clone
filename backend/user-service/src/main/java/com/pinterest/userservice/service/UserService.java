package com.pinterest.userservice.service;

import com.pinterest.userservice.domain.User;
import com.pinterest.userservice.dto.RegisterRequest;
import com.pinterest.userservice.dto.UserResponse;
import com.pinterest.userservice.exception.DuplicateResourceException;
import com.pinterest.userservice.exception.InvalidCredentialsException;
import com.pinterest.userservice.repository.UserRepository;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final ModelMapper modelMapper;

  @Transactional
  public UserResponse register(RegisterRequest request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new DuplicateResourceException("Email is already in use");
    }
    if (userRepository.existsByUsername(request.getUsername())) {
      throw new DuplicateResourceException("Username is already in use");
    }

    User user = User.builder()
        .email(request.getEmail())
        .username(request.getUsername())
        .passwordHash(passwordEncoder.encode(request.getPassword()))
        .createdAt(Instant.now())
        .build();
    User saved = userRepository.save(user);
    return modelMapper.map(saved, UserResponse.class);
  }

  public UserResponse authenticate(String email, String rawPassword) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
    if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
      throw new InvalidCredentialsException("Invalid email or password");
    }
    return modelMapper.map(user, UserResponse.class);
  }
}
