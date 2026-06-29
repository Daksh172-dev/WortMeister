import { useMutation, useQuery } from "@tanstack/react-query";
import { CheckCircle } from "lucide-react-native";
import { Text, View } from "react-native";
import { api } from "@/shared/api/client";
import { AppButton, Card, PremiumHeader, Screen, Skeleton } from "@/shared/components/ui";

export function LessonScreen() {
  const lessons = useQuery({ queryKey: ["lessons"], queryFn: async () => (await api.get("/learning/lessons")).data.data });
  const lesson = lessons.data?.[0];
  const complete = useMutation({ mutationFn: async () => (await api.post("/learning/lessons/complete", { lessonId: lesson?.id, score: 88 })).data.data });
  return (
    <Screen>
      <PremiumHeader eyebrow="Lesson" title={lesson?.title ?? "Daily German"} subtitle="A clean lesson player with progress, offline-ready content, and adaptive completion." />
      <View className="px-5">
        {lessons.isLoading ? <Skeleton className="h-52" /> : (
          <Card>
            <Text className="text-2xl font-black text-paper">{lesson?.title ?? "No lesson cached"}</Text>
            <Text className="mt-3 text-base leading-7 text-white/70">{lesson?.content ?? "Open once online to cache your next lesson."}</Text>
          </Card>
        )}
        <View className="mt-4">
          <AppButton icon={CheckCircle} label="Complete with 88%" loading={complete.isPending} onPress={() => complete.mutate()} />
        </View>
      </View>
    </Screen>
  );
}
