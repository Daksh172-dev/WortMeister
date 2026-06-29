package com.wortmeister.learning;

import com.wortmeister.identity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PronunciationAttemptRepository extends JpaRepository<PronunciationAttempt, Long> {
    int countByUser(User user);
}
