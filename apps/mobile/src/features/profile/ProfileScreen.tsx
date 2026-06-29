import { useNavigation } from "@react-navigation/native";
import { Bell, LogOut, Moon, Settings, Trophy } from "lucide-react-native";
import { Text, View } from "react-native";
import { useQuery } from "@tanstack/react-query";
import { api } from "@/shared/api/client";
import { AppButton, Card, PremiumHeader, Screen } from "@/shared/components/ui";
import { useAuthStore } from "@/shared/state/authStore";
import { useThemeStore } from "@/shared/state/themeStore";

export function ProfileScreen() {
  const navigation = useNavigation<any>();
  const clear = useAuthStore((state) => state.clear);
  const toggleTheme = useThemeStore((state) => state.toggleTheme);
  const profile = useQuery({ queryKey: ["me"], queryFn: async () => (await api.get("/me")).data.data });
  return (
    <Screen>
      <PremiumHeader eyebrow="Profile" title={profile.data?.displayName ?? "Learner"} subtitle="Your progress identity across lessons, practice, rewards, and settings." />
      <View className="gap-3 px-5">
        <Card>
          <Text className="text-3xl font-black text-paper">{profile.data?.xp ?? 0} XP</Text>
          <Text className="mt-2 text-white/60">{profile.data?.coins ?? 0} coins · {profile.data?.cefrLevel ?? "A1"} level</Text>
        </Card>
        <AppButton icon={Trophy} label="Achievements" tone="ghost" onPress={() => navigation.navigate("Achievements")} />
        <AppButton icon={Bell} label="Notifications" tone="ghost" onPress={() => navigation.navigate("Notifications")} />
        <AppButton icon={Settings} label="Settings" tone="ghost" onPress={() => navigation.navigate("Settings")} />
        <AppButton icon={Moon} label="Toggle dark mode" tone="ghost" onPress={toggleTheme} />
        <AppButton icon={LogOut} label="Log out" tone="danger" onPress={clear} />
      </View>
    </Screen>
  );
}
