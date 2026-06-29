package com.wortmeister.learning;

import com.wortmeister.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "vocabulary_items")
public class VocabularyItem extends BaseEntity {
    @Column(nullable = false)
    private String german;
    @Column(nullable = false)
    private String english;
    @Column(nullable = false)
    private String article;
    @Column(nullable = false)
    private String exampleSentence;
    @Column(nullable = false, length = 16)
    private String cefrLevel;

    protected VocabularyItem() {
    }

    public VocabularyItem(String german, String english, String article, String exampleSentence, String cefrLevel) {
        this.german = german;
        this.english = english;
        this.article = article;
        this.exampleSentence = exampleSentence;
        this.cefrLevel = cefrLevel;
    }

    public String getGerman() {
        return german;
    }

    public String getEnglish() {
        return english;
    }

    public String getArticle() {
        return article;
    }

    public String getExampleSentence() {
        return exampleSentence;
    }

    public String getCefrLevel() {
        return cefrLevel;
    }
}
