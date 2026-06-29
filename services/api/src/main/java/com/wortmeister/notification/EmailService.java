package com.wortmeister.notification;

import com.wortmeister.identity.User;

public interface EmailService {
    void sendEmailVerification(User user, String rawToken);
    void sendPasswordReset(User user, String rawToken);
}
