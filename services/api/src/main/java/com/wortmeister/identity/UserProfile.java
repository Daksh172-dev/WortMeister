package com.wortmeister.identity;

import com.wortmeister.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_profiles")
public class UserProfile extends BaseEntity {
    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, length = 120)
    private String displayName = "Learner";

    private String avatarUrl;

    @Column(nullable = false)
    private int xp;

    @Column(nullable = false)
    private int coins;

    @Column(nullable = false)
    private int streakDays;

    @Column(nullable = false, length = 16)
    private String cefrLevel = "A1";

    @Column(nullable = false)
    private boolean dailyReminderEnabled = true;

    @Column(nullable = false)
    private boolean soundEffectsEnabled = true;

    protected UserProfile() {
    }

    public UserProfile(User user) {
        this.user = user;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public int getXp() {
        return xp;
    }

    public int getCoins() {
        return coins;
    }

    public int getStreakDays() {
        return streakDays;
    }

    public String getCefrLevel() {
        return cefrLevel;
    }

    public boolean isDailyReminderEnabled() {
        return dailyReminderEnabled;
    }

    public boolean isSoundEffectsEnabled() {
        return soundEffectsEnabled;
    }

    public void updateProfile(String displayName, String cefrLevel) {
        this.displayName = displayName;
        this.cefrLevel = cefrLevel;
        touch();
    }

    public void updateAvatar(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        touch();
    }

    public void updateSettings(boolean dailyReminderEnabled, boolean soundEffectsEnabled) {
        this.dailyReminderEnabled = dailyReminderEnabled;
        this.soundEffectsEnabled = soundEffectsEnabled;
        touch();
    }

    public void addRewards(int xp, int coins) {
        this.xp += xp;
        this.coins += coins;
        this.streakDays += 1;
        touch();
    }
}
