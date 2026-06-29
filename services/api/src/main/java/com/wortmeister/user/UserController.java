package com.wortmeister.user;

import com.wortmeister.common.web.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/me")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    ApiResponse<UserDtos.ProfileResponse> me(Authentication authentication) {
        return ApiResponse.of(userService.profile(authentication));
    }

    @PatchMapping("/profile")
    ApiResponse<UserDtos.ProfileResponse> updateProfile(Authentication authentication,
            @Valid @RequestBody UserDtos.UpdateProfileRequest request) {
        return ApiResponse.of(userService.update(authentication, request));
    }

    @PatchMapping("/avatar")
    ApiResponse<UserDtos.ProfileResponse> updateAvatar(Authentication authentication,
            @Valid @RequestBody UserDtos.AvatarRequest request) {
        return ApiResponse.of(userService.avatar(authentication, request));
    }

    @PatchMapping("/settings")
    ApiResponse<UserDtos.ProfileResponse> updateSettings(Authentication authentication,
            @Valid @RequestBody UserDtos.SettingsRequest request) {
        return ApiResponse.of(userService.settings(authentication, request));
    }

    @GetMapping("/statistics")
    ApiResponse<UserDtos.StatisticsResponse> statistics(Authentication authentication) {
        return ApiResponse.of(userService.statistics(authentication));
    }
}
