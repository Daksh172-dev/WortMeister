package com.wortmeister.notification;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public List<NotificationDtos.NotificationResponse> notifications() {
        return List.of(
                new NotificationDtos.NotificationResponse("streak", "Keep your streak", "Complete one German lesson today.", false),
                new NotificationDtos.NotificationResponse("review", "Vocabulary review", "Your A1 words are ready for practice.", false)
        );
    }

    public void registerDevice(NotificationDtos.DeviceTokenRequest request) {
        if (request.token() == null || request.token().isBlank()) {
            throw new IllegalArgumentException("Device token is required.");
        }
    }
}
