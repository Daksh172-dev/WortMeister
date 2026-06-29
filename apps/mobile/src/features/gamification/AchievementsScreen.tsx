import { FeatureScreen } from "@/shared/components/FeatureScreen";

export function AchievementsScreen() {
  return <FeatureScreen title="Achievements" eyebrow="Rewards" subtitle="Collect milestone badges and unlock rewards." endpoint="/gamification/achievements" />;
}
