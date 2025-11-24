package com.pinterest.userservice.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

  @NotBlank(message = "Please provide a valid email")
  @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.(com|org|in)$",
      message = "Please provide a valid email")
  private String email;

  @NotBlank(message = "Please provide a valid username")
  @Pattern(regexp = "^[a-z0-9._-]+$", message = "Username must be lowercase letters, digits, or ._- characters")
  private String username;

  @NotBlank(message = "Please provide a valid password")
  @Size(min = 8, max = 16, message = "Password must be between 8 and 16 characters")
  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$",
      message = "Password must include upper, lower, digit, and special character")
  private String password;

  @NotBlank(message = "Please confirm your password")
  private String confirmPassword;

  @AssertTrue(message = "Passwords do not match")
  public boolean isMatchingPasswords() {
    return password != null && password.equals(confirmPassword);
  }
}
