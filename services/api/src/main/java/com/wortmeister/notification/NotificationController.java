package com.wortmeister.notification;

import com.wortmeister.common.web.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    ApiResponse<List<NotificationDtos.NotificationResponse>> notifications() {
        return ApiResponse.of(notificationService.notifications());
    }

    @PostMapping("/device-token")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void registerDevice(@Valid @RequestBody NotificationDtos.DeviceTokenRequest request) {
        notificationService.registerDevice(request);
    }
}
