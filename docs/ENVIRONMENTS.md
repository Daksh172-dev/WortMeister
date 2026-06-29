# Environment Management and Secrets

## Environments

- `local`: developer machines and Docker Compose.
- `staging`: production-like validation environment.
- `production`: customer-facing deployment.

## Secret Sources

- Local: `.env` files created from `.env.example`.
- GitHub Actions: repository or environment secrets.
- Production: cloud secret manager or orchestrator-managed secrets.

## Required Secrets

- `JWT_SECRET`
- `AI_INTERNAL_API_KEY`
- `INTERNAL_API_KEY`
- `DB_PASSWORD`
- `RABBITMQ_PASSWORD`
- `GOOGLE_CLIENT_ID`
- `OPENAI_API_KEY` or `GEMINI_API_KEY`

## Rotation

1. Add new secret version.
2. Deploy services accepting the new value.
3. Revoke old value after active sessions expire.
4. Record the rotation in the audit log or operations journal.
