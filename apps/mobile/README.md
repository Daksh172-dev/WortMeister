# WortMeister Mobile

Production-oriented Expo React Native client for the WortMeister German learning platform.

## Stack

- Expo + React Native + TypeScript
- React Navigation
- React Query
- Axios
- Zustand
- React Hook Form
- NativeWind
- MMKV storage
- Expo Notifications
- Expo AV for recording/playback
- Expo Image Picker

## Environment

Create `.env` or pass Expo public variables:

```bash
EXPO_PUBLIC_API_URL=http://localhost:8080/api/v1
```

## Commands

```bash
npm install
npm run start
npm run typecheck
```

## Architecture

The app uses feature-based MVVM-style organization:

- `src/features/*`: screen-level features and view logic.
- `src/shared/api`: Axios and React Query infrastructure.
- `src/shared/components`: reusable UI primitives.
- `src/shared/hooks`: media, notifications, responsive, and offline hooks.
- `src/shared/state`: Zustand stores.
- `src/shared/theme`: design tokens.

All app screens call the Spring Boot backend API. AI functionality is accessed through backend `/ai/*` endpoints so the mobile app never talks directly to the internal FastAPI service.
