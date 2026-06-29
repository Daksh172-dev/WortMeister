import { create } from "zustand";
import { persist, createJSONStorage } from "zustand/middleware";
import { appStorage } from "@/shared/storage/mmkv";

type AuthState = {
  accessToken?: string;
  refreshToken?: string;
  hasCompletedOnboarding: boolean;
  setTokens: (accessToken: string, refreshToken: string) => void;
  clear: () => void;
  completeOnboarding: () => void;
};

export const useAuthStore = create<AuthState>()(
  persist(
    (set) => ({
      hasCompletedOnboarding: false,
      setTokens: (accessToken, refreshToken) => set({ accessToken, refreshToken }),
      clear: () => set({ accessToken: undefined, refreshToken: undefined }),
      completeOnboarding: () => set({ hasCompletedOnboarding: true })
    }),
    { name: "wm-auth", storage: createJSONStorage(() => appStorage) }
  )
);
