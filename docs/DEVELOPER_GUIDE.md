# Developer Guide

## Backend

```bash
gradle :services:api:test
gradle :services:api:bootRun
```

## AI Service

```bash
cd services/ai-service
python -m pip install ".[dev]"
pytest
uvicorn app.main:app --reload
```

## Mobile

```bash
cd apps/mobile
npm install
npm run typecheck
npm run start
```

## Coding Standards

- Keep modules feature-based and avoid leaking persistence details into controllers.
- Validate all request DTOs.
- Add tests near changed behavior.
- Use structured logs and correlation IDs.
- Do not commit secrets, local build output, or generated caches.

## Branching

Use short feature branches:

```text
feature/auth-refresh-hardening
feature/mobile-speaking-practice
fix/ai-cache-serialization
```
