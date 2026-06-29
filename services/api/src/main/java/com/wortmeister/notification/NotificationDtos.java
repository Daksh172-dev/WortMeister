package com.wortmeister.notification;

public final class NotificationDtos {
    private NotificationDtos() {
    }

    public record NotificationResponse(String id, String title, String body, boolean read) {
    }

    public record DeviceTokenRequest(String token, String platform) {
    }
}
