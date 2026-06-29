import axios, { AxiosError, InternalAxiosRequestConfig } from "axios";
import { useAuthStore } from "@/shared/state/authStore";

export const API_BASE_URL = process.env.EXPO_PUBLIC_API_URL ?? "http://localhost:8080/api/v1";

export const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 20_000,
  headers: { "Content-Type": "application/json" }
});

api.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  const token = useAuthStore.getState().accessToken;
  config.headers["X-Correlation-Id"] = `${Date.now()}-${Math.random().toString(16).slice(2)}`;
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  async (error: AxiosError) => {
    const original = error.config as (InternalAxiosRequestConfig & { _retry?: boolean }) | undefined;
    const refreshToken = useAuthStore.getState().refreshToken;
    if (error.response?.status === 401 && original && !original._retry && refreshToken) {
      original._retry = true;
      const response = await axios.post(`${API_BASE_URL}/auth/refresh`, { refreshToken });
      const tokens = response.data.data;
      useAuthStore.getState().setTokens(tokens.accessToken, tokens.refreshToken);
      original.headers.Authorization = `Bearer ${tokens.accessToken}`;
      return api(original);
    }
    return Promise.reject(error);
  }
);

export type ApiEnvelope<T> = { data: T };
