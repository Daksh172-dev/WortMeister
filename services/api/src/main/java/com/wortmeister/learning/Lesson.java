package com.wortmeister.learning;

import com.wortmeister.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "lessons")
public class Lesson extends BaseEntity {
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, length = 16)
    private String cefrLevel;
    @Column(nullable = false, length = 64)
    private String category;
    @Column(nullable = false, length = 4000)
    private String content;

    protected Lesson() {
    }

    public Lesson(String title, String cefrLevel, String category, String content) {
        this.title = title;
        this.cefrLevel = cefrLevel;
        this.category = category;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getCefrLevel() {
        return cefrLevel;
    }

    public String getCategory() {
        return category;
    }

    public String getContent() {
        return content;
    }

    public void update(String title, String cefrLevel, String category, String content) {
        this.title = title;
        this.cefrLevel = cefrLevel;
        this.category = category;
        this.content = content;
        touch();
    }
}
