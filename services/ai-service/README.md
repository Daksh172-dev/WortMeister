# WortMeister AI Service

FastAPI microservice for AI vocabulary extraction, image recognition, grammar correction, pronunciation feedback, adaptive learning, quiz and flashcard generation, embeddings, semantic search, translation fallback, and tutor chat.

## Run Locally

```bash
uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

Set `INTERNAL_API_KEY` and send it as `X-Internal-Api-Key` from the Spring Boot backend.

## Environment

- `INTERNAL_API_KEY`: required shared secret for internal calls.
- `OPENAI_API_KEY`: optional OpenAI provider key.
- `GEMINI_API_KEY`: optional Gemini provider key.
- `REDIS_URL`: optional Redis cache URL.
- `EMBEDDING_MODEL_NAME`: sentence-transformers model name.
- `WHISPER_MODEL`: production speech model hint.

