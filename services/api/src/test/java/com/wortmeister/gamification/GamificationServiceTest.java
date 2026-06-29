package com.wortmeister.gamification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.wortmeister.identity.UserRepository;
import com.wortmeister.user.UserService;
import org.junit.jupiter.api.Test;

class GamificationServiceTest {
    @Test
    void dailyQuestsContainActionableItems() {
        GamificationService service = new GamificationService(
                mock(UserService.class),
                mock(UserRepository.class),
                mock(AchievementRepository.class)
        );

        assertThat(service.dailyQuests()).isNotEmpty();
        assertThat(service.weeklyChallenges()).isNotEmpty();
    }
}
