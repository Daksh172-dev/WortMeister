package com.wortmeister.auth;

public record GoogleIdentity(String subject, String email, boolean emailVerified, String displayName, String avatarUrl) {
}
