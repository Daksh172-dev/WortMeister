# Database Documentation

Database: MySQL 8.x  
Migration tool: Flyway  
Primary schema owner: Spring Boot API

## Schema Groups

- Identity: `users`, `user_profiles`, `refresh_tokens`, `one_time_tokens`
- Learning: `lessons`, `vocabulary_items`, `grammar_topics`, `media_assets`, `learning_progress`, `pronunciation_attempts`
- Gamification: `achievements`
- AI: `ai_request_logs`
- Audit: `audit_logs`

## ER Diagram

```mermaid
erDiagram
  users ||--|| user_profiles : has
  users ||--o{ refresh_tokens : owns
  users ||--o{ one_time_tokens : receives
  users ||--o{ learning_progress : completes
  users ||--o{ pronunciation_attempts : records
  users ||--o{ ai_request_logs : requests
  lessons ||--o{ learning_progress : tracked_by
  achievements ||--o{ users : rewards
  audit_logs }o--|| users : actor
```

## Migration Rules

- Every schema change must be a Flyway migration.
- Migrations must be backward compatible for normal releases.
- Destructive changes require a backup, migration window, and rollback plan.
- Public IDs are exposed externally; numeric IDs remain internal.

## SQL Injection Protection

The backend uses Spring Data JPA repositories and parameter binding. Raw SQL should be avoided unless reviewed and covered by tests.
