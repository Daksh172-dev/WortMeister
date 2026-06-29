package com.wortmeister.learning;

import com.wortmeister.identity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LearningProgressRepository extends JpaRepository<LearningProgress, Long> {
    @Query("select count(p) from LearningProgress p where p.user = :user and p.completed = true")
    int countCompletedLessons(User user);

    @Query("select count(p) from LearningProgress p where p.user = :user and p.score >= 90")
    int countMasteredVocabulary(User user);
}
