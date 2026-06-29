package com.wortmeister.learning;

import com.wortmeister.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "media_assets")
public class MediaAsset extends BaseEntity {
    @Column(nullable = false)
    private String url;
    @Column(nullable = false, length = 32)
    private String type;
    @Column(nullable = false)
    private String description;

    protected MediaAsset() {
    }

    public MediaAsset(String url, String type, String description) {
        this.url = url;
        this.type = type;
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
