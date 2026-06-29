# API Documentation

## Public Mobile API

Base path: `/api/v1`

Authentication:

- `POST /auth/register`
- `POST /auth/login`
- `POST /auth/oauth/google`
- `POST /auth/refresh`
- `POST /auth/logout`
- `POST /auth/verify-email`
- `POST /auth/forgot-password`
- `POST /auth/reset-password`

User:

- `GET /me`
- `PATCH /me/profile`
- `PATCH /me/avatar`
- `PATCH /me/settings`
- `GET /me/statistics`

Learning:

- `GET /learning/lessons`
- `GET /learning/lessons/{lessonId}`
- `POST /learning/lessons/complete`
- `GET /learning/vocabulary`
- `GET /learning/grammar`
- `GET /learning/images`
- `GET /learning/audio`
- `POST /learning/pronunciation/evaluate`

Gamification:

- `GET /gamification/level`
- `GET /gamification/daily-quests`
- `GET /gamification/weekly-challenges`
- `GET /gamification/leaderboard`
- `GET /gamification/achievements`
- `POST /gamification/rewards/daily`

AI:

- `POST /ai/lessons/personalized`
- `POST /ai/vocabulary/recommend`
- `POST /ai/pronunciation/evaluate`
- `POST /ai/images/explain`
- `POST /ai/conversations/generate`
- `POST /ai/flashcards/generate`
- `POST /ai/tutor/chat`

Notifications:

- `GET /notifications`
- `POST /notifications/device-token`

## Admin API

Base path: `/api/admin/v1`

- `GET /users`
- `POST /lessons`
- `PUT /lessons/{lessonId}`
- `GET /analytics`
- `GET /dashboard`
- `GET /audit-logs`
- `GET /reports`

## Internal AI API

Base path: `/internal/v1`

All requests require `X-Internal-Api-Key`.

- `POST /vocabulary/extract`
- `POST /images/recognize-scene`
- `POST /vocabulary/contextual`
- `POST /conversations/generate`
- `POST /grammar/correct`
- `POST /pronunciation/score`
- `POST /speaking/feedback`
- `POST /learning/path`
- `POST /learning/adaptive-difficulty`
- `POST /quizzes/generate`
- `POST /flashcards/generate`
- `POST /embeddings`
- `POST /words/similar`
- `POST /translation/fallback`
- `POST /tutor/chat`

## Error Format

All backend errors use a stable problem response with `code`, `message`, `status`, and `correlationId`.
