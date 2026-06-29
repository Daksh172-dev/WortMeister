import { useMutation } from "@tanstack/react-query";
import { FeatureScreen } from "@/shared/components/FeatureScreen";
import { api } from "@/shared/api/client";

export function FlashcardsScreen() {
  const generate = useMutation({
    mutationFn: async () => api.post("/ai/flashcards/generate", { sourceText: "Der Tisch steht im Zimmer.", cefrLevel: "A1", cardCount: 8 })
  });
  return <FeatureScreen title="Flashcards" eyebrow="Memory" subtitle="Premium spaced-repetition cards generated from lessons." endpoint="/learning/vocabulary" actions={[{ label: generate.isPending ? "Generating" : "Generate", onPress: () => generate.mutate() }]} />;
}
