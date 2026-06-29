import { FeatureScreen } from "@/shared/components/FeatureScreen";

export function VocabularyScreen() {
  return <FeatureScreen title="Vocabulary" eyebrow="Words" subtitle="Contextual German words, examples, and review cues." endpoint="/learning/vocabulary" searchable paginated />;
}
