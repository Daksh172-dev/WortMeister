import { create } from "zustand";
import { persist, createJSONStorage } from "zustand/middleware";
import { appStorage } from "@/shared/storage/mmkv";
import type { ThemeName } from "@/shared/theme/tokens";

type ThemeState = {
  theme: ThemeName;
  setTheme: (theme: ThemeName) => void;
  toggleTheme: () => void;
};

export const useThemeStore = create<ThemeState>()(
  persist(
    (set, get) => ({
      theme: "dark",
      setTheme: (theme) => set({ theme }),
      toggleTheme: () => set({ theme: get().theme === "dark" ? "light" : "dark" })
    }),
    { name: "wm-theme", storage: createJSONStorage(() => appStorage) }
  )
);
