package com.wortmeister.identity;

import com.wortmeister.common.domain.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Column(nullable = false, unique = true, length = 320)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private boolean emailVerified;

    @Column(nullable = false)
    private boolean enabled = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private UserRole role = UserRole.LEARNER;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserProfile profile;

    protected User() {
    }

    public User(String email, String passwordHash) {
        this.email = email.toLowerCase();
        this.passwordHash = passwordHash;
        this.profile = new UserProfile(this);
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void verifyEmail() {
        this.emailVerified = true;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public UserRole getRole() {
        return role;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void grantAdmin() {
        this.role = UserRole.ADMIN;
    }
}
