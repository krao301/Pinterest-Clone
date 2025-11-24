package com.pinterest.auth.controller;

import com.pinterest.auth.model.User;
import com.pinterest.auth.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    record RegisterRequest(@Email String email,
                           @NotBlank @Size(min = 3) String username,
                           @NotBlank @Size(min = 8, max = 64) String password) {}

    record LoginRequest(@Email String email, @NotBlank String password) {}

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final com.pinterest.auth.service.LoginAttemptService loginAttemptService;
    private final com.pinterest.auth.service.RefreshTokenService refreshTokenService;

    public AuthController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, com.pinterest.auth.service.LoginAttemptService loginAttemptService, com.pinterest.auth.service.RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.loginAttemptService = loginAttemptService;
        this.refreshTokenService = refreshTokenService;
    }

    @GetMapping("/lock-status")
    public ResponseEntity<?> lockStatus(@RequestParam String email) {
        var remaining = loginAttemptService.getRemainingLockMillis(email);
        var isOpen = remaining > 0;
        return ResponseEntity.ok(Map.of("isOpen", isOpen, "remainingMillis", remaining));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        if (userRepository.findByEmail(req.email()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Email already in use"));
        }
        if (userRepository.findByUsername(req.username()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Username already in use"));
        }
        var user = new User();
        user.setEmail(req.email());
        user.setUsername(req.username());
        user.setPasswordHash(passwordEncoder.encode(req.password()));
        userRepository.save(user);

        // generate token on registration and create refresh token cookie
        var jwtSecret = System.getenv().getOrDefault("JWT_SECRET", "defaultsecretkeydefaultsecretsecret");
        var jwtUtil = new com.pinterest.auth.security.JwtUtil(jwtSecret);
        var token = jwtUtil.generateToken(String.valueOf(user.getId()), 1000L * 60L * 60L);
        var refresh = refreshTokenService.create(user.getId());

        var userMap = Map.of("id", user.getId(), "email", user.getEmail(), "username", user.getUsername());
        var data = Map.of("token", token, "user", userMap);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refresh.getToken())
            .httpOnly(true)
            .secure(Boolean.parseBoolean(System.getenv().getOrDefault("COOKIE_SECURE","false")))
            .path("/")
            .maxAge(60L * 60L * 24L * 30L)
            .sameSite("Lax")
            .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(Map.of("success", true, "data", data));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        // check lockout
        var remaining = loginAttemptService.getRemainingLockMillis(req.email());
        if (remaining > 0) {
            return ResponseEntity.status(429).body(Map.of("success", false, "message", "Too many failed attempts", "remainingMillis", remaining));
        }

        var opt = userRepository.findByEmail(req.email());
        if (opt.isEmpty()) {
            loginAttemptService.recordFailure(req.email());
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Invalid credentials"));
        }
        var user = opt.get();
        if (!passwordEncoder.matches(req.password(), user.getPasswordHash())) {
            loginAttemptService.recordFailure(req.email());
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Invalid credentials"));
        }
        // success
        loginAttemptService.recordSuccess(req.email());
        // generate JWT
        var jwtSecret = System.getenv().getOrDefault("JWT_SECRET", "defaultsecretkeydefaultsecretkey");
        var jwtUtil = new com.pinterest.auth.security.JwtUtil(jwtSecret);
        var token = jwtUtil.generateToken(String.valueOf(user.getId()), 1000L * 60L * 60L); // 1 hour

        // create refresh token and set as httpOnly cookie
        var refresh = refreshTokenService.create(user.getId());

        var userMap = Map.of("id", user.getId(), "email", user.getEmail(), "username", user.getUsername());
        var data = Map.of("token", token, "user", userMap);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refresh.getToken())
                .httpOnly(true)
                .secure(Boolean.parseBoolean(System.getenv().getOrDefault("COOKIE_SECURE","false")))
                .path("/")
                .maxAge(60L * 60L * 24L * 30L)
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(Map.of("success", true, "data", data));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue(name = "refreshToken", required = false) String refreshToken) {
        if (refreshToken == null) return ResponseEntity.status(401).body(Map.of("success", false, "message", "missing refresh token cookie"));
        var opt = refreshTokenService.findByToken(refreshToken);
        if (opt.isEmpty() || opt.get().isRevoked() || opt.get().getExpiresAt().isBefore(java.time.Instant.now())) {
            // clear cookie
            ResponseCookie clear = ResponseCookie.from("refreshToken", "")
                    .httpOnly(true)
                    .secure(Boolean.parseBoolean(System.getenv().getOrDefault("COOKIE_SECURE","false")))
                    .path("/")
                    .maxAge(0)
                    .sameSite("Lax")
                    .build();
            return ResponseEntity.status(401).header(HttpHeaders.SET_COOKIE, clear.toString()).body(Map.of("success", false, "message", "Invalid refresh token"));
        }
        var userId = opt.get().getUserId();
        var jwtSecret = System.getenv().getOrDefault("JWT_SECRET", "defaultsecretkeydefaultsecretsecret");
        var jwtUtil = new com.pinterest.auth.security.JwtUtil(jwtSecret);
        var token = jwtUtil.generateToken(String.valueOf(userId), 1000L * 60L * 60L);
        return ResponseEntity.ok(Map.of("success", true, "data", Map.of("token", token)));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(name = "refreshToken", required = false) String refreshToken) {
        if (refreshToken != null) refreshTokenService.revoke(refreshToken);
        ResponseCookie clear = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(Boolean.parseBoolean(System.getenv().getOrDefault("COOKIE_SECURE","false")))
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, clear.toString()).body(Map.of("success", true));
    }
}
