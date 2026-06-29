package com.wortmeister.notification;

import com.wortmeister.identity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoggingEmailService implements EmailService {
    private static final Logger log = LoggerFactory.getLogger(LoggingEmailService.class);

    @Override
    public void sendEmailVerification(User user, String rawToken) {
        log.info("email_verification_requested userId={} tokenPresent={}", user.getPublicId(), rawToken != null);
    }

    @Override
    public void sendPasswordReset(User user, String rawToken) {
        log.info("password_reset_requested userId={} tokenPresent={}", user.getPublicId(), rawToken != null);
    }
}
