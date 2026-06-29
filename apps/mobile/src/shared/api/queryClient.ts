import AsyncStorage from "@react-native-async-storage/async-storage";
import { QueryClient } from "@tanstack/react-query";

export const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 60_000,
      gcTime: 1000 * 60 * 60 * 24,
      retry: 2,
      refetchOnReconnect: true
    },
    mutations: {
      retry: 1
    }
  }
});

export const offlineKeys = {
  mutationQueue: "wm-offline-mutations"
};

export async function enqueueOfflineMutation(item: unknown) {
  const current = await AsyncStorage.getItem(offlineKeys.mutationQueue);
  const list = current ? JSON.parse(current) : [];
  list.push(item);
  await AsyncStorage.setItem(offlineKeys.mutationQueue, JSON.stringify(list));
}
