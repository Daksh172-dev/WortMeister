package com.wortmeister.gamification;

import com.wortmeister.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "achievements")
public class Achievement extends BaseEntity {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private int rewardCoins;

    protected Achievement() {
    }

    public Achievement(String name, String description, int rewardCoins) {
        this.name = name;
        this.description = description;
        this.rewardCoins = rewardCoins;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getRewardCoins() {
        return rewardCoins;
    }
}
