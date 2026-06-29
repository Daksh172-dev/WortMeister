import React from "react";
import * as Notifications from "expo-notifications";
import { SchedulableTriggerInputTypes } from "expo-notifications";
import { useQuery } from "@tanstack/react-query";
import { BellRing } from "lucide-react-native";
import { Text, View } from "react-native";
import { api } from "@/shared/api/client";
import { AppButton, Card, PremiumHeader, Screen } from "@/shared/components/ui";

export function NotificationsScreen() {
  const notifications = useQuery({ 
    queryKey: ["notifications"], 
    queryFn: async () => (await api.get("/notifications")).data.data 
  });

  async function schedule() {
    await Notifications.scheduleNotificationAsync({
      content: { title: "WortMeister", body: "Your German streak is waiting." },
      trigger: { 
        type: SchedulableTriggerInputTypes.TIME_INTERVAL,
        seconds: 5 
      }
    });
  }
  
  return (
    <Screen>
      <PremiumHeader eyebrow="Notifications" title="Stay in rhythm." subtitle="Gentle reminders for streaks, reviews, weekly challenges, and speaking practice." />
      <View className="gap-3 px-5">
        {notifications.data?.map((item: { id: string; title: string; body: string }) => (
          <Card key={item.id}>
            <Text className="text-lg font-black text-paper">{item.title}</Text>
            <Text className="mt-2 text-white/60">{item.body}</Text>
          </Card>
        ))}
        <Card>
          <AppButton icon={BellRing} label="Preview reminder" onPress={schedule} />
        </Card>
      </View>
    </Screen>
  );
}
