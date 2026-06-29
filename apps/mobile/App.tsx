import "react-native-reanimated";

import { NavigationContainer } from "@react-navigation/native";
import { QueryClientProvider } from "@tanstack/react-query";
import { StatusBar } from "expo-status-bar";
import { useEffect } from "react";
import { ErrorBoundary } from "@/shared/components/ErrorBoundary";
import { AppNavigator } from "@/app/navigation/AppNavigator";
import { queryClient } from "@/shared/api/queryClient";
import { useThemeStore } from "@/shared/state/themeStore";
import { useNotificationSetup } from "@/shared/hooks/useNotificationSetup";
import { useOfflineSync } from "@/shared/hooks/useOfflineSync";

export default function App() {
  const theme = useThemeStore((state) => state.theme);
  useNotificationSetup();
  useOfflineSync();

  useEffect(() => {
    queryClient.resumePausedMutations().then(() => queryClient.invalidateQueries());
  }, []);

  return (
    <ErrorBoundary>
      <QueryClientProvider client={queryClient}>
        <NavigationContainer>
          <StatusBar style={theme === "dark" ? "light" : "dark"} />
          <AppNavigator />
        </NavigationContainer>
      </QueryClientProvider>
    </ErrorBoundary>
  );
}
