import { useNavigation } from "@react-navigation/native";
import { Image, Layers, MessageCircle, Rows3, Search } from "lucide-react-native";
import { Text, View } from "react-native";
import { AppButton, Card, PremiumHeader, Screen } from "@/shared/components/ui";

export function DiscoverScreen() {
  const navigation = useNavigation<any>();
  const items = [
    ["Vocabulary", "High-signal word sets with contextual memory.", "Vocabulary", Search],
    ["Grammar", "Notion-like grammar cards for scanning and practice.", "Grammar", Rows3],
    ["Image Learning", "Upload scenes and learn visual vocabulary.", "ImageLearning", Image],
    ["Flashcards", "Fast spaced repetition loops.", "Flashcards", Layers],
    ["Conversation", "Spotify-like practice sessions for real scenarios.", "ConversationPractice", MessageCircle]
  ] as const;
  return (
    <Screen>
      <PremiumHeader eyebrow="Discover" title="Pick a mode." subtitle="Move between structured lessons and AI-assisted practice without losing momentum." />
      <View className="gap-3 px-5">
        {items.map(([title, text, route, Icon]) => (
          <Card key={title}>
            <View className="flex-row items-center justify-between">
              <View className="mr-3 flex-1">
                <Text className="text-xl font-black text-paper">{title}</Text>
                <Text className="mt-1 text-white/60">{text}</Text>
              </View>
              <AppButton icon={Icon} label="Open" tone="ghost" onPress={() => navigation.navigate(route)} />
            </View>
          </Card>
        ))}
      </View>
    </Screen>
  );
}
