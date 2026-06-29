# Disaster Recovery and Backup Strategy

## Recovery Objectives

- RPO: 15 minutes for production database.
- RTO: 2 hours for core learning API.
- AI features may degrade while the core learning platform remains available.

## Backups

- MySQL: automated daily full backup plus point-in-time recovery where managed database supports it.
- Redis: treated as recoverable cache; persistence is enabled locally but not a source of truth.
- Object storage: versioned buckets with lifecycle rules.
- Configuration: environment files are reproducible from secret manager records.

## Restore Procedure

1. Freeze deployments.
2. Identify last valid database backup.
3. Restore MySQL into a clean instance.
4. Re-run application health checks.
5. Rebuild Redis caches through normal traffic.
6. Replay RabbitMQ dead-letter jobs when safe.
7. Document incident timeline and preventive action.
