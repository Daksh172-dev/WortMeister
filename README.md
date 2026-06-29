# WortMeister

WortMeister is an enterprise-grade AI-powered German language learning platform built as a full-stack software engineering project. It combines a React Native mobile app, a Spring Boot backend, a FastAPI AI service, MySQL, Redis, RabbitMQ, Docker, monitoring, CI/CD, and deployment documentation.

## Highlights

- Mobile app: Expo React Native, TypeScript, Zustand, React Query, Axios, NativeWind, MMKV.
- Backend: Spring Boot 3, Java 21, Spring Security, JWT, OAuth2-ready Google login, JPA, Flyway, Redis, RabbitMQ, OpenAPI.
- AI service: FastAPI, OpenAI/Gemini abstraction, Whisper-ready transcription, sentence embeddings, FAISS, Redis cache.
- DevOps: Docker, Docker Compose, Nginx, GitHub Actions, Prometheus, Grafana, ELK-style logging.
- Testing: JUnit, Mockito, PyTest, React Native Testing Library, Postman collection, k6 load testing.
- Documentation: architecture, ER/UML diagrams, database, deployment, developer, installation, user, and contribution guides.

## Repository Layout

```text
apps/mobile          Expo React Native application
apps/admin           React admin portal
services/api         Spring Boot backend
services/ai-service  FastAPI AI microservice
infra                Docker Compose, Nginx, monitoring, load testing
docs                 Architecture and project documentation
postman              API collection
.github/workflows    CI/CD automation
```

## Quick Start

```bash
docker compose -f infra/compose/docker-compose.local.yml up --build
```

Then run each app as needed:

```bash
cd services/ai-service
python -m uvicorn app.main:app --reload

cd apps/mobile
npm install
npm run start
```

Backend Swagger UI is available at `/swagger-ui.html` when the API is running.

## Documentation

- [Architecture Guide](docs/ARCHITECTURE.md)
- [API Documentation](docs/API.md)
- [Database Documentation](docs/DATABASE.md)
- [Deployment Guide](docs/DEPLOYMENT.md)
- [Developer Guide](docs/DEVELOPER_GUIDE.md)
- [Installation Guide](docs/INSTALLATION.md)
- [User Manual](docs/USER_MANUAL.md)
- [Contributing Guide](docs/CONTRIBUTING.md)
- [Security Guide](docs/SECURITY.md)
- [Environment Management](docs/ENVIRONMENTS.md)
- [Disaster Recovery](docs/DISASTER_RECOVERY.md)
