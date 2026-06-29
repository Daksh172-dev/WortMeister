package com.wortmeister.audit;

import com.wortmeister.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "audit_logs")
public class AuditLog extends BaseEntity {
    @Column(nullable = false, length = 120)
    private String actor;
    @Column(nullable = false, length = 120)
    private String action;
    @Column(nullable = false, length = 120)
    private String resource;
    @Column(nullable = false, length = 2000)
    private String metadata;

    protected AuditLog() {
    }

    public AuditLog(String actor, String action, String resource, String metadata) {
        this.actor = actor;
        this.action = action;
        this.resource = resource;
        this.metadata = metadata;
    }

    public String getActor() {
        return actor;
    }

    public String getAction() {
        return action;
    }

    public String getResource() {
        return resource;
    }

    public String getMetadata() {
        return metadata;
    }
}
