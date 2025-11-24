package com.pinterest.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

  @NotBlank(message = "Please provide a valid email")
  @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.(com|org|in)$",
      message = "Please provide a valid email")
  private String email;

  @NotBlank(message = "Please provide a valid password")
  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,16}$",
      message = "Password must include upper, lower, digit, and special character")
  private String password;
}
