package com.wortmeister.gamification;

import com.wortmeister.identity.User;
import com.wortmeister.identity.UserRepository;
import com.wortmeister.user.UserService;
import java.util.Comparator;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GamificationService {
    private final UserService userService;
    private final UserRepository users;
    private final AchievementRepository achievements;

    public GamificationService(UserService userService, UserRepository users, AchievementRepository achievements) {
        this.userService = userService;
        this.users = users;
        this.achievements = achievements;
    }

    @Transactional(readOnly = true)
    public GamificationDtos.LevelResponse level(Authentication authentication) {
        int xp = userService.currentUser(authentication).getProfile().getXp();
        int level = Math.max(1, xp / 500 + 1);
        return new GamificationDtos.LevelResponse(xp, level, level * 500);
    }

    public List<GamificationDtos.QuestResponse> dailyQuests() {
        return List.of(
                new GamificationDtos.QuestResponse("daily-lesson", "Complete one lesson", "Finish any lesson today.", 30, false),
                new GamificationDtos.QuestResponse("daily-word", "Review vocabulary", "Review 10 German words.", 20, false));
    }

    public List<GamificationDtos.QuestResponse> weeklyChallenges() {
        return List.of(new GamificationDtos.QuestResponse("weekly-speaking", "Speaking sprint",
                "Complete 5 pronunciation attempts this week.", 150, false));
    }

    @Transactional(readOnly = true)
    public List<GamificationDtos.LeaderboardEntry> leaderboard() {
        List<User> ranked = users.findAll().stream()
                .sorted(Comparator.comparing((User u) -> u.getProfile().getXp()).reversed())
                .limit(50)
                .toList();
        return java.util.stream.IntStream.range(0, ranked.size())
                .mapToObj(i -> new GamificationDtos.LeaderboardEntry(ranked.get(i).getPublicId(),
                        ranked.get(i).getProfile().getDisplayName(), ranked.get(i).getProfile().getXp(), i + 1))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<GamificationDtos.AchievementResponse> achievements() {
        return achievements.findAll().stream()
                .map(a -> new GamificationDtos.AchievementResponse(a.getPublicId(), a.getName(), a.getDescription(), a.getRewardCoins()))
                .toList();
    }

    @Transactional
    public GamificationDtos.RewardResponse claimDaily(Authentication authentication) {
        User user = userService.currentUser(authentication);
        user.getProfile().addRewards(15, 5);
        return new GamificationDtos.RewardResponse("DAILY", 15, 5, "Daily reward claimed.");
    }
}
