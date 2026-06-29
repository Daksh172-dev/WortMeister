import * as Notifications from "expo-notifications";
import { Platform } from "react-native";
import { useEffect } from "react";
import { api } from "@/shared/api/client";

Notifications.setNotificationHandler({
  handleNotification: async () => ({
    shouldShowAlert: true,
    shouldPlaySound: false,
    shouldSetBadge: true,
    shouldShowBanner: true,
    shouldShowList: true
  })
});

export function useNotificationSetup() {
  useEffect(() => {
    async function setup() {
      const permission = await Notifications.requestPermissionsAsync();
      if (!permission.granted) return;
      try {
        const token = await Notifications.getExpoPushTokenAsync();
        await api.post("/notifications/device-token", {
          token: token.data,
          platform: Platform.OS
        });
      } catch {
        // Push token registration is best-effort and retried on future app launches.
      }
    }
    setup();
  }, []);
}
