import NetInfo from "@react-native-community/netinfo";
import AsyncStorage from "@react-native-async-storage/async-storage";
import { useEffect } from "react";
import { api } from "@/shared/api/client";
import { offlineKeys } from "@/shared/api/queryClient";
import { useNetworkStore } from "@/shared/state/networkStore";

type QueuedMutation = { method: "post" | "patch" | "put"; path: string; body: unknown };

export function useOfflineSync() {
  useEffect(() => {
    return NetInfo.addEventListener(async (state) => {
      useNetworkStore.getState().setOnline(Boolean(state.isConnected));
      if (!state.isConnected) return;
      const raw = await AsyncStorage.getItem(offlineKeys.mutationQueue);
      const queue: QueuedMutation[] = raw ? JSON.parse(raw) : [];
      if (!queue.length) return;
      const remaining: QueuedMutation[] = [];
      for (const item of queue) {
        try {
          await api[item.method](item.path, item.body);
        } catch {
          remaining.push(item);
        }
      }
      await AsyncStorage.setItem(offlineKeys.mutationQueue, JSON.stringify(remaining));
    });
  }, []);
}
