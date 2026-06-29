package com.wortmeister.identity;

import com.wortmeister.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "one_time_tokens")
public class OneTimeToken extends BaseEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true)
    private String tokenHash;

    @Column(nullable = false, length = 32)
    private String purpose;

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private boolean consumed;

    protected OneTimeToken() {
    }

    public OneTimeToken(User user, String tokenHash, String purpose, Instant expiresAt) {
        this.user = user;
        this.tokenHash = tokenHash;
        this.purpose = purpose;
        this.expiresAt = expiresAt;
    }

    public User getUser() {
        return user;
    }

    public boolean validFor(String purpose) {
        return this.purpose.equals(purpose) && !consumed && expiresAt.isAfter(Instant.now());
    }

    public void consume() {
        this.consumed = true;
        touch();
    }
}
