package com.wortmeister.learning;

import com.wortmeister.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "grammar_topics")
public class GrammarTopic extends BaseEntity {
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, length = 16)
    private String cefrLevel;
    @Column(nullable = false, length = 4000)
    private String explanation;

    protected GrammarTopic() {
    }

    public GrammarTopic(String title, String cefrLevel, String explanation) {
        this.title = title;
        this.cefrLevel = cefrLevel;
        this.explanation = explanation;
    }

    public String getTitle() {
        return title;
    }

    public String getCefrLevel() {
        return cefrLevel;
    }

    public String getExplanation() {
        return explanation;
    }
}
