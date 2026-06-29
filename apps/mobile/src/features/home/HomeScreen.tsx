import { useNavigation } from "@react-navigation/native";
import { BookOpen, Flame, Sparkles } from "lucide-react-native";
import { Text, View } from "react-native";
import { useQuery } from "@tanstack/react-query";
import { api } from "@/shared/api/client";
import { AppButton, Card, HeroBand, PremiumHeader, Screen, Skeleton } from "@/shared/components/ui";

export function HomeScreen() {
  const navigation = useNavigation<any>();
  const profile = useQuery({ queryKey: ["me"], queryFn: async () => (await api.get("/me")).data.data });
  const stats = useQuery({ queryKey: ["stats"], queryFn: async () => (await api.get("/me/statistics")).data.data });

  return (
    <Screen>
      <PremiumHeader eyebrow="Heute" title={`Hallo, ${profile.data?.displayName ?? "Learner"}`} subtitle="Your learning day is compact, focused, and ready." />
      <HeroBand>
        <View className="flex-row items-center justify-between">
          <View>
            <Text className="text-sm font-bold uppercase text-white/70">Daily streak</Text>
            <Text className="mt-1 text-4xl font-black text-white">{profile.data?.streakDays ?? 0} days</Text>
          </View>
          <Flame color="white" size={42} />
        </View>
      </HeroBand>
      <View className="gap-3 px-5 pt-5">
        {stats.isLoading ? <Skeleton className="h-28" /> : (
          <Card>
            <Text className="text-xl font-black text-paper">{stats.data?.xp ?? 0} XP · {stats.data?.coins ?? 0} coins</Text>
            <Text className="mt-2 text-white/60">Lessons, speaking reps, vocabulary mastery, and streaks sync here.</Text>
          </Card>
        )}
        <AppButton icon={BookOpen} label="Continue lesson" onPress={() => navigation.navigate("Lesson")} />
        <AppButton icon={Sparkles} label="Open AI practice" tone="ghost" onPress={() => navigation.navigate("ConversationPractice")} />
      </View>
    </Screen>
  );
}
