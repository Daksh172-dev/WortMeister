package com.wortmeister.auth;

import static org.assertj.core.api.Assertions.assertThat;

import com.wortmeister.WortMeisterApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = WortMeisterApplication.class)
@ActiveProfiles("test")
class AuthServiceTest {
    @Autowired
    private AuthService authService;

    @Test
    void registerReturnsAccessAndRefreshTokens() {
        AuthDtos.TokenResponse response = authService.register(
                new AuthDtos.RegisterRequest("learner@wortmeister.local", "superSecret123", "Ada"));

        assertThat(response.accessToken()).isNotBlank();
        assertThat(response.refreshToken()).isNotBlank();
        assertThat(response.tokenType()).isEqualTo("Bearer");
    }
}
