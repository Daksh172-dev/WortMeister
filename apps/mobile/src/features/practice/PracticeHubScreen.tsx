import { useNavigation } from "@react-navigation/native";
import { Mic, MessageCircle, Trophy } from "lucide-react-native";
import { AppButton, PremiumHeader, Screen } from "@/shared/components/ui";
import { View } from "react-native";

export function PracticeHubScreen() {
  const navigation = useNavigation<any>();
  return (
    <Screen>
      <PremiumHeader eyebrow="Practice" title="Train the hard parts." subtitle="Speaking, conversation, leaderboard pressure, and achievements in one focused hub." />
      <View className="gap-3 px-5">
        <AppButton icon={Mic} label="Speaking Practice" onPress={() => navigation.navigate("SpeakingPractice")} />
        <AppButton icon={MessageCircle} label="Conversation Practice" tone="ghost" onPress={() => navigation.navigate("ConversationPractice")} />
        <AppButton icon={Trophy} label="Leaderboard" tone="ghost" onPress={() => navigation.navigate("Leaderboard")} />
      </View>
    </Screen>
  );
}
