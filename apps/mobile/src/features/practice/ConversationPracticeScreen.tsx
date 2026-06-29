import { useMutation } from "@tanstack/react-query";
import { Send } from "lucide-react-native";
import { Text, View } from "react-native";
import { api } from "@/shared/api/client";
import { AppButton, Card, PremiumHeader, Screen } from "@/shared/components/ui";

export function ConversationPracticeScreen() {
  const conversation = useMutation({
    mutationFn: async () => (await api.post("/ai/conversations/generate", { cefrLevel: "A1", scenario: "ordering coffee in Berlin" })).data.data
  });
  return (
    <Screen>
      <PremiumHeader eyebrow="AI Tutor" title="Conversation Practice" subtitle="Role-play German scenarios with corrections, hints, and next-turn suggestions." />
      <View className="gap-3 px-5">
        <AppButton icon={Send} label="Generate dialogue" loading={conversation.isPending} onPress={() => conversation.mutate()} />
        <Card>
          <Text className="text-lg font-black text-paper">Berlin Cafe</Text>
          <Text className="mt-2 leading-6 text-white/60">{conversation.data?.result ?? "Tap generate to start an adaptive conversation."}</Text>
        </Card>
      </View>
    </Screen>
  );
}
