package com.wortmeister.auth;

import com.wortmeister.common.error.ApiException;
import com.wortmeister.common.security.JwtService;
import com.wortmeister.common.security.SecurityProperties;
import com.wortmeister.identity.OneTimeToken;
import com.wortmeister.identity.OneTimeTokenRepository;
import com.wortmeister.identity.RefreshToken;
import com.wortmeister.identity.RefreshTokenRepository;
import com.wortmeister.identity.User;
import com.wortmeister.identity.UserRepository;
import com.wortmeister.notification.EmailService;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.HexFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private static final String EMAIL_VERIFY = "EMAIL_VERIFY";
    private static final String PASSWORD_RESET = "PASSWORD_RESET";

    private final UserRepository users;
    private final RefreshTokenRepository refreshTokens;
    private final OneTimeTokenRepository oneTimeTokens;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final SecurityProperties properties;
    private final AuthenticationManager authenticationManager;
    private final GoogleTokenVerifier googleTokenVerifier;
    private final EmailService emailService;
    private final SecureRandom random = new SecureRandom();

    public AuthService(UserRepository users, RefreshTokenRepository refreshTokens, OneTimeTokenRepository oneTimeTokens,
            PasswordEncoder passwordEncoder, JwtService jwtService, SecurityProperties properties,
            AuthenticationManager authenticationManager, GoogleTokenVerifier googleTokenVerifier, EmailService emailService) {
        this.users = users;
        this.refreshTokens = refreshTokens;
        this.oneTimeTokens = oneTimeTokens;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.properties = properties;
        this.authenticationManager = authenticationManager;
        this.googleTokenVerifier = googleTokenVerifier;
        this.emailService = emailService;
    }

    @Transactional
    public AuthDtos.TokenResponse register(AuthDtos.RegisterRequest request) {
        if (users.existsByEmailIgnoreCase(request.email())) {
            throw new ApiException(HttpStatus.CONFLICT, "EMAIL_ALREADY_REGISTERED", "Email is already registered.");
        }
        User user = new User(request.email(), passwordEncoder.encode(request.password()));
        user.getProfile().updateProfile(request.displayName(), "A1");
        users.save(user);
        String verificationToken = createOneTimeToken(user, EMAIL_VERIFY, 24);
        emailService.sendEmailVerification(user, verificationToken);
        return tokensFor(user);
    }

    @Transactional
    public AuthDtos.TokenResponse login(AuthDtos.LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        User user = users.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", "Invalid credentials."));
        return tokensFor(user);
    }

    @Transactional
    public AuthDtos.TokenResponse googleLogin(AuthDtos.GoogleLoginRequest request) {
        GoogleIdentity identity = googleTokenVerifier.verify(request.idToken());
        User user = users.findByEmailIgnoreCase(identity.email())
                .orElseGet(() -> users.save(new User(identity.email(), passwordEncoder.encode(randomToken()))));
        user.getProfile().updateProfile(identity.displayName(), user.getProfile().getCefrLevel());
        if (identity.avatarUrl() != null && !identity.avatarUrl().isBlank()) {
            user.getProfile().updateAvatar(identity.avatarUrl());
        }
        user.verifyEmail();
        return tokensFor(user);
    }

    @Transactional
    public AuthDtos.TokenResponse refresh(AuthDtos.RefreshRequest request) {
        RefreshToken token = refreshTokens.findByTokenHash(hash(request.refreshToken()))
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "INVALID_REFRESH_TOKEN", "Invalid refresh token."));
        if (!token.isActive()) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "INVALID_REFRESH_TOKEN", "Invalid refresh token.");
        }
        token.revoke();
        return tokensFor(token.getUser());
    }

    @Transactional
    public void verifyEmail(AuthDtos.VerifyEmailRequest request) {
        OneTimeToken token = oneTimeTokens.findByTokenHash(hash(request.token()))
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "INVALID_TOKEN", "Invalid verification token."));
        if (!token.validFor(EMAIL_VERIFY)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "INVALID_TOKEN", "Invalid verification token.");
        }
        token.getUser().verifyEmail();
        token.consume();
    }

    @Transactional
    public void forgotPassword(AuthDtos.ForgotPasswordRequest request) {
        users.findByEmailIgnoreCase(request.email()).ifPresent(user -> {
            String resetToken = createOneTimeToken(user, PASSWORD_RESET, 2);
            emailService.sendPasswordReset(user, resetToken);
        });
    }

    @Transactional
    public void resetPassword(AuthDtos.ResetPasswordRequest request) {
        OneTimeToken token = oneTimeTokens.findByTokenHash(hash(request.token()))
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "INVALID_TOKEN", "Invalid reset token."));
        if (!token.validFor(PASSWORD_RESET)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "INVALID_TOKEN", "Invalid reset token.");
        }
        token.getUser().setPasswordHash(passwordEncoder.encode(request.newPassword()));
        token.consume();
    }

    @Transactional
    public void logout(AuthDtos.RefreshRequest request) {
        refreshTokens.findByTokenHash(hash(request.refreshToken())).ifPresent(RefreshToken::revoke);
    }

    private AuthDtos.TokenResponse tokensFor(User user) {
        String rawRefreshToken = randomToken();
        refreshTokens.save(new RefreshToken(user, hash(rawRefreshToken),
                Instant.now().plusSeconds(properties.refreshTokenDays() * 86_400)));
        return new AuthDtos.TokenResponse(jwtService.createAccessToken(user), rawRefreshToken, "Bearer",
                properties.accessTokenMinutes() * 60);
    }

    private String createOneTimeToken(User user, String purpose, int hours) {
        String rawToken = randomToken();
        oneTimeTokens.save(new OneTimeToken(user, hash(rawToken), purpose, Instant.now().plusSeconds(hours * 3600L)));
        return rawToken;
    }

    private String randomToken() {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return HexFormat.of().formatHex(bytes);
    }

    private String hash(String value) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                    .digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to hash token", ex);
        }
    }
}
