import { FeatureScreen } from "@/shared/components/FeatureScreen";

export function LeaderboardScreen() {
  return <FeatureScreen title="Leaderboard" eyebrow="League" subtitle="A weekly ladder for XP, consistency, and courage." endpoint="/gamification/leaderboard" paginated />;
}
