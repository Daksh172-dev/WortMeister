import { useMutation } from "@tanstack/react-query";
import { Bell, Volume2 } from "lucide-react-native";
import { View } from "react-native";
import { api } from "@/shared/api/client";
import { AppButton, PremiumHeader, Screen } from "@/shared/components/ui";

export function SettingsScreen() {
  const save = useMutation({
    mutationFn: async (body: { dailyReminderEnabled: boolean; soundEffectsEnabled: boolean }) => api.patch("/me/settings", body)
  });
  return (
    <Screen>
      <PremiumHeader eyebrow="Settings" title="Make it yours." subtitle="Control reminders, sound, sync behavior, and accessibility preferences." />
      <View className="gap-3 px-5">
        <AppButton icon={Bell} label="Enable daily reminders" onPress={() => save.mutate({ dailyReminderEnabled: true, soundEffectsEnabled: true })} />
        <AppButton icon={Volume2} label="Quiet mode" tone="ghost" onPress={() => save.mutate({ dailyReminderEnabled: true, soundEffectsEnabled: false })} />
      </View>
    </Screen>
  );
}
