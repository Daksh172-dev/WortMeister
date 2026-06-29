package com.wortmeister.gamification;

import com.wortmeister.common.web.ApiResponse;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/gamification")
public class GamificationController {
    private final GamificationService gamificationService;

    public GamificationController(GamificationService gamificationService) {
        this.gamificationService = gamificationService;
    }

    @GetMapping("/level")
    ApiResponse<GamificationDtos.LevelResponse> level(Authentication authentication) {
        return ApiResponse.of(gamificationService.level(authentication));
    }

    @GetMapping("/daily-quests")
    ApiResponse<List<GamificationDtos.QuestResponse>> dailyQuests() {
        return ApiResponse.of(gamificationService.dailyQuests());
    }

    @GetMapping("/weekly-challenges")
    ApiResponse<List<GamificationDtos.QuestResponse>> weeklyChallenges() {
        return ApiResponse.of(gamificationService.weeklyChallenges());
    }

    @GetMapping("/leaderboard")
    ApiResponse<List<GamificationDtos.LeaderboardEntry>> leaderboard() {
        return ApiResponse.of(gamificationService.leaderboard());
    }

    @GetMapping("/achievements")
    ApiResponse<List<GamificationDtos.AchievementResponse>> achievements() {
        return ApiResponse.of(gamificationService.achievements());
    }

    @PostMapping("/rewards/daily")
    ApiResponse<GamificationDtos.RewardResponse> claimDaily(Authentication authentication) {
        return ApiResponse.of(gamificationService.claimDaily(authentication));
    }
}
