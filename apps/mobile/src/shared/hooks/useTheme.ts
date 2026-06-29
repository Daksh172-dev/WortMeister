import { useThemeStore } from "@/shared/state/themeStore";
import { themes } from "@/shared/theme/tokens";

export function useTheme() {
  const themeName = useThemeStore((state) => state.theme);
  return { themeName, colors: themes[themeName], isDark: themeName === "dark" };
}
