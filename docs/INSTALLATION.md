# Installation Guide

## Prerequisites

- Docker Desktop
- Java 21
- Gradle 8+
- Python 3.11+
- Node.js 22+
- Android Studio or Xcode for native mobile builds

## Clone

```bash
git clone <repository-url>
cd wortmeister
```

## Configure

Copy environment templates:

```bash
cp apps/mobile/.env.example apps/mobile/.env
cp services/ai-service/.env.example services/ai-service/.env
```

Set strong values for:

- `JWT_SECRET`
- `AI_INTERNAL_API_KEY`
- `INTERNAL_API_KEY`
- `GOOGLE_CLIENT_ID`
- AI provider keys

## Start Infrastructure

```bash
docker compose -f infra/compose/docker-compose.local.yml up --build
```
