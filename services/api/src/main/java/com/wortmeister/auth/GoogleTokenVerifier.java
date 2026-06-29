package com.wortmeister.auth;

public interface GoogleTokenVerifier {
    GoogleIdentity verify(String idToken);
}
