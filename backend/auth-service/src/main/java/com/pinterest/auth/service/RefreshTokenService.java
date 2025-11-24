package com.pinterest.auth.service;

import com.pinterest.auth.model.RefreshToken;
import com.pinterest.auth.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository repository;

    public RefreshTokenService(RefreshTokenRepository repository) {
        this.repository = repository;
    }

    public RefreshToken create(Long userId) {
        var token = new RefreshToken();
        token.setUserId(userId);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiresAt(Instant.now().plus(30, ChronoUnit.DAYS));
        token.setRevoked(false);
        return repository.save(token);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return repository.findByToken(token);
    }

    public void revoke(String token) {
        repository.findByToken(token).ifPresent(t -> { t.setRevoked(true); repository.save(t); });
    }
}
