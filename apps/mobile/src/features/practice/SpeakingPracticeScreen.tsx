import { useMutation } from "@tanstack/react-query";
import { Mic, Play, Square } from "lucide-react-native";
import { Text, View } from "react-native";
import { api } from "@/shared/api/client";
import { AppButton, Card, PremiumHeader, Screen } from "@/shared/components/ui";
import { useVoiceRecorder } from "@/shared/hooks/useMedia";

export function SpeakingPracticeScreen() {
  const recorder = useVoiceRecorder();
  const evaluate = useMutation({
    mutationFn: async () => (await api.post("/learning/pronunciation/evaluate", { phrase: "Ich mochte einen Kaffee.", audioUrl: recorder.uri ?? "local://recording" })).data.data
  });
  return (
    <Screen>
      <PremiumHeader eyebrow="Speak" title="Pronunciation Studio" subtitle="Record, replay, and score German speech with specific improvement cues." />
      <View className="gap-3 px-5">
        <Card>
          <Text className="text-xl font-black text-paper">Ich mochte einen Kaffee.</Text>
          <Text className="mt-2 text-white/60">Listen, record, compare, repeat.</Text>
        </Card>
        <AppButton icon={recorder.isRecording ? Square : Mic} label={recorder.isRecording ? "Stop recording" : "Record"} onPress={recorder.isRecording ? recorder.stop : recorder.start} />
        <AppButton icon={Play} label="Play recording" tone="ghost" onPress={recorder.play} />
        <AppButton label="Evaluate" tone="ghost" loading={evaluate.isPending} onPress={() => evaluate.mutate()} />
        <Card>
          <Text className="font-black text-paper">Score: {evaluate.data?.score ?? "--"}</Text>
          <Text className="mt-2 text-white/60">{evaluate.data?.feedback ?? "Your feedback appears here."}</Text>
        </Card>
      </View>
    </Screen>
  );
}
