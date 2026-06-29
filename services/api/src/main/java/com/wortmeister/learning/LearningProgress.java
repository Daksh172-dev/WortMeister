package com.wortmeister.learning;

import com.wortmeister.common.domain.BaseEntity;
import com.wortmeister.identity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "learning_progress")
public class LearningProgress extends BaseEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(optional = false)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;
    @Column(nullable = false)
    private int score;
    @Column(nullable = false)
    private boolean completed;

    protected LearningProgress() {
    }

    public LearningProgress(User user, Lesson lesson, int score, boolean completed) {
        this.user = user;
        this.lesson = lesson;
        this.score = score;
        this.completed = completed;
    }

    public int getScore() {
        return score;
    }

    public boolean isCompleted() {
        return completed;
    }
}
