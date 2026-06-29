package com.wortmeister.user;

import com.wortmeister.common.error.NotFoundException;
import com.wortmeister.identity.User;
import com.wortmeister.identity.UserProfile;
import com.wortmeister.identity.UserRepository;
import com.wortmeister.learning.LearningProgressRepository;
import com.wortmeister.learning.PronunciationAttemptRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository users;
    private final LearningProgressRepository progress;
    private final PronunciationAttemptRepository pronunciationAttempts;

    public UserService(UserRepository users, LearningProgressRepository progress,
            PronunciationAttemptRepository pronunciationAttempts) {
        this.users = users;
        this.progress = progress;
        this.pronunciationAttempts = pronunciationAttempts;
    }

    @Transactional(readOnly = true)
    public User currentUser(Authentication authentication) {
        return users.findByEmailIgnoreCase(authentication.getName()).orElseThrow(() -> new NotFoundException("User not found."));
    }

    @Transactional(readOnly = true)
    public UserDtos.ProfileResponse profile(Authentication authentication) {
        return toProfile(currentUser(authentication));
    }

    @Transactional
    public UserDtos.ProfileResponse update(Authentication authentication, UserDtos.UpdateProfileRequest request) {
        User user = currentUser(authentication);
        user.getProfile().updateProfile(request.displayName(), request.cefrLevel());
        return toProfile(user);
    }

    @Transactional
    public UserDtos.ProfileResponse avatar(Authentication authentication, UserDtos.AvatarRequest request) {
        User user = currentUser(authentication);
        user.getProfile().updateAvatar(request.avatarUrl());
        return toProfile(user);
    }

    @Transactional
    public UserDtos.ProfileResponse settings(Authentication authentication, UserDtos.SettingsRequest request) {
        User user = currentUser(authentication);
        user.getProfile().updateSettings(request.dailyReminderEnabled(), request.soundEffectsEnabled());
        return toProfile(user);
    }

    @Transactional(readOnly = true)
    public UserDtos.StatisticsResponse statistics(Authentication authentication) {
        User user = currentUser(authentication);
        UserProfile profile = user.getProfile();
        return new UserDtos.StatisticsResponse(profile.getXp(), profile.getCoins(), profile.getStreakDays(),
                progress.countCompletedLessons(user), progress.countMasteredVocabulary(user),
                pronunciationAttempts.countByUser(user));
    }

    private UserDtos.ProfileResponse toProfile(User user) {
        UserProfile profile = user.getProfile();
        return new UserDtos.ProfileResponse(user.getPublicId(), user.getEmail(), profile.getDisplayName(),
                profile.getAvatarUrl(), profile.getCefrLevel(), profile.getXp(), profile.getCoins(),
                profile.getStreakDays(), user.isEmailVerified());
    }
}
