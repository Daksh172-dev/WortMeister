package com.wortmeister.gamification;

public final class GamificationDtos {
    private GamificationDtos() {
    }

    public record LevelResponse(int xp, int level, int nextLevelXp) {
    }

    public record QuestResponse(String id, String title, String description, int rewardXp, boolean completed) {
    }

    public record LeaderboardEntry(String userId, String displayName, int xp, int rank) {
    }

    public record AchievementResponse(String id, String name, String description, int rewardCoins) {
    }

    public record RewardResponse(String type, int xp, int coins, String message) {
    }
}
