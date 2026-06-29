import { Text, View } from "react-native";
import { Check, Target } from "lucide-react-native";
import { AppButton, Card, PremiumHeader, Screen } from "@/shared/components/ui";
import { useAuthStore } from "@/shared/state/authStore";

export function OnboardingScreen() {
  const complete = useAuthStore((state) => state.completeOnboarding);
  return (
    <Screen>
      <PremiumHeader eyebrow="Onboarding" title="Tune your path." subtitle="Choose a rhythm, then WortMeister blends lessons, review, speaking, and AI practice into a daily plan." />
      <View className="gap-3 px-5">
        {["A1 foundations", "Travel confidence", "Speak clearly", "Pass a German exam"].map((item) => (
          <Card key={item}>
            <View className="flex-row items-center">
              <Target color="white" />
              <Text className="ml-3 text-lg font-black text-paper">{item}</Text>
            </View>
          </Card>
        ))}
        <AppButton icon={Check} label="Build my path" onPress={complete} />
      </View>
    </Screen>
  );
}
