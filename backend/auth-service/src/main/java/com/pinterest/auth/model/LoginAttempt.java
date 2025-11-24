package com.pinterest.auth.model;

import jakarta.persistence.*;

@Entity
@Table(name = "login_attempts", indexes = {@Index(columnList = "email", unique = true)})
public class LoginAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private int failedCount = 0;

    @Column(nullable = false)
    private long firstFailedAt = 0L;

    @Column(nullable = false)
    private long lockedUntil = 0L;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getFailedCount() { return failedCount; }
    public void setFailedCount(int failedCount) { this.failedCount = failedCount; }
    public long getFirstFailedAt() { return firstFailedAt; }
    public void setFirstFailedAt(long firstFailedAt) { this.firstFailedAt = firstFailedAt; }
    public long getLockedUntil() { return lockedUntil; }
    public void setLockedUntil(long lockedUntil) { this.lockedUntil = lockedUntil; }
}
