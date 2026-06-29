package com.wortmeister.learning;

import com.wortmeister.common.domain.BaseEntity;
import com.wortmeister.identity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "pronunciation_attempts")
public class PronunciationAttempt extends BaseEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false)
    private String phrase;
    @Column(nullable = false)
    private String audioUrl;
    @Column(nullable = false)
    private int score;
    @Column(nullable = false, length = 1000)
    private String feedback;

    protected PronunciationAttempt() {
    }

    public PronunciationAttempt(User user, String phrase, String audioUrl, int score, String feedback) {
        this.user = user;
        this.phrase = phrase;
        this.audioUrl = audioUrl;
        this.score = score;
        this.feedback = feedback;
    }

    public int getScore() {
        return score;
    }

    public String getFeedback() {
        return feedback;
    }
}
