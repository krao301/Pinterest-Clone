package com.pinterest.auth.service;

import com.pinterest.auth.model.LoginAttempt;
import com.pinterest.auth.repository.LoginAttemptRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginAttemptService {

    private final LoginAttemptRepository repository;

    public LoginAttemptService(LoginAttemptRepository repository) {
        this.repository = repository;
    }

    public void recordFailure(String email) {
        var now = System.currentTimeMillis();
        Optional<LoginAttempt> opt = repository.findByEmail(email);
        LoginAttempt attempt;
        if (opt.isPresent()) {
            attempt = opt.get();
            if (attempt.getFirstFailedAt() == 0 || now - attempt.getFirstFailedAt() > 30000L) {
                attempt.setFailedCount(1);
                attempt.setFirstFailedAt(now);
            } else {
                attempt.setFailedCount(attempt.getFailedCount() + 1);
            }
        } else {
            attempt = new LoginAttempt();
            attempt.setEmail(email);
            attempt.setFailedCount(1);
            attempt.setFirstFailedAt(now);
            attempt.setLockedUntil(0L);
        }

        if (attempt.getFailedCount() >= 3) {
            attempt.setLockedUntil(now + 60000L);
        }
        repository.save(attempt);
    }

    public void recordSuccess(String email) {
        repository.deleteByEmail(email);
    }

    public long getRemainingLockMillis(String email) {
        Optional<LoginAttempt> opt = repository.findByEmail(email);
        if (opt.isEmpty()) return 0L;
        var attempt = opt.get();
        var now = System.currentTimeMillis();
        if (attempt.getLockedUntil() > now) return attempt.getLockedUntil() - now;
        return 0L;
    }
}
