package com.wortmeister.ai;

import com.wortmeister.common.domain.BaseEntity;
import com.wortmeister.identity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ai_request_logs")
public class AiRequestLog extends BaseEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false, length = 64)
    private String feature;
    @Column(nullable = false, length = 4000)
    private String prompt;
    @Column(nullable = false, length = 4000)
    private String response;

    protected AiRequestLog() {
    }

    public AiRequestLog(User user, String feature, String prompt, String response) {
        this.user = user;
        this.feature = feature;
        this.prompt = prompt;
        this.response = response;
    }
}
