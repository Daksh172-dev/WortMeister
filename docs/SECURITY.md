# Security Guide

## Authentication

- JWT access tokens are short lived.
- Refresh tokens are opaque, hashed, rotating, and revocable.
- Passwords use BCrypt.
- Google login validates provider-issued identity tokens server-side.

## Authorization

- Learner endpoints require authentication.
- Admin endpoints require `ROLE_ADMIN`.
- Internal AI endpoints require `X-Internal-Api-Key`.

## Hardening

- Security headers are configured in Spring Security and Nginx.
- API rate limiting is backed by Redis.
- Query input sanitization rejects common XSS and SQL injection probes.
- JPA repositories provide parameter binding for database access.
- CSRF is ignored for stateless mobile `/api/**` routes; future cookie-based web admin routes should enable CSRF tokens.
- Secrets are injected via environment variables or CI secret stores.

## Logging Rules

Never log:

- Passwords
- JWTs or refresh tokens
- OAuth tokens
- AI provider keys
- Signed upload URLs
- Unredacted sensitive prompts
