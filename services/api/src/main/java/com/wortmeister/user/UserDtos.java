package com.wortmeister.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class UserDtos {
    private UserDtos() {
    }

    public record ProfileResponse(String id, String email, String displayName, String avatarUrl, String cefrLevel,
                                  int xp, int coins, int streakDays, boolean emailVerified) {
    }

    public record UpdateProfileRequest(@NotBlank @Size(max = 120) String displayName,
                                       @NotBlank @Size(max = 16) String cefrLevel) {
    }

    public record AvatarRequest(@NotBlank String avatarUrl) {
    }

    public record SettingsRequest(boolean dailyReminderEnabled, boolean soundEffectsEnabled) {
    }

    public record StatisticsResponse(int xp, int coins, int streakDays, int lessonsCompleted,
                                     int vocabularyMastered, int pronunciationAttempts) {
    }
}
