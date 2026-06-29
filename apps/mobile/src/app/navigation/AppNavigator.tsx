import React from "react";
import { createBottomTabNavigator } from "@react-navigation/bottom-tabs";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import { Bell, Compass, Home, Mic, Settings, Trophy, User } from "lucide-react-native";
import { useAuthStore } from "@/shared/state/authStore";
import { useTheme } from "@/shared/hooks/useTheme";
import { AuthScreen } from "@/features/auth/AuthScreen";
import { OnboardingScreen } from "@/features/onboarding/OnboardingScreen";
import { HomeScreen } from "@/features/home/HomeScreen";
import { DiscoverScreen } from "@/features/discover/DiscoverScreen";
import { PracticeHubScreen } from "@/features/practice/PracticeHubScreen";
import { ProfileScreen } from "@/features/profile/ProfileScreen";
import { LessonScreen } from "@/features/learning/LessonScreen";
import { VocabularyScreen } from "@/features/learning/VocabularyScreen";
import { GrammarScreen } from "@/features/learning/GrammarScreen";
import { ImageLearningScreen } from "@/features/learning/ImageLearningScreen";
import { FlashcardsScreen } from "@/features/learning/FlashcardsScreen";
import { ConversationPracticeScreen } from "@/features/practice/ConversationPracticeScreen";
import { SpeakingPracticeScreen } from "@/features/practice/SpeakingPracticeScreen";
import { LeaderboardScreen } from "@/features/gamification/LeaderboardScreen";
import { AchievementsScreen } from "@/features/gamification/AchievementsScreen";
import { SettingsScreen } from "@/features/settings/SettingsScreen";
import { NotificationsScreen } from "@/features/notifications/NotificationsScreen";
import type { MainTabParamList, RootStackParamList } from "@/app/navigation/types";

const Stack = createNativeStackNavigator<RootStackParamList>();
const Tabs = createBottomTabNavigator<MainTabParamList>();

function MainTabs() {
  const { colors } = useTheme();
  return (
    <Tabs.Navigator
      screenOptions={{
        headerShown: false,
        tabBarStyle: { backgroundColor: colors.surface, borderTopColor: colors.border, height: 72 },
        tabBarActiveTintColor: colors.accent,
        tabBarInactiveTintColor: colors.muted,
        tabBarLabelStyle: { fontWeight: "700" }
      }}
    >
      <Tabs.Screen name="Home" component={HomeScreen} options={{ tabBarIcon: ({ color }: { color: string }) => <Home color={color} /> }} />
      <Tabs.Screen name="Discover" component={DiscoverScreen} options={{ tabBarIcon: ({ color }: { color: string }) => <Compass color={color} /> }} />
      <Tabs.Screen name="Practice" component={PracticeHubScreen} options={{ tabBarIcon: ({ color }: { color: string }) => <Mic color={color} /> }} />
      <Tabs.Screen name="Profile" component={ProfileScreen} options={{ tabBarIcon: ({ color }: { color: string }) => <User color={color} /> }} />
    </Tabs.Navigator>
  );
}

export function AppNavigator() {
  const { colors } = useTheme();
  const accessToken = useAuthStore((state: any) => state.accessToken);
  const onboarded = useAuthStore((state: any) => state.hasCompletedOnboarding);

  return (
    <Stack.Navigator screenOptions={{ headerShown: false, contentStyle: { backgroundColor: colors.background } }}>
      {!accessToken ? <Stack.Screen name="Auth" component={AuthScreen} /> : !onboarded ? <Stack.Screen name="Onboarding" component={OnboardingScreen} /> : <Stack.Screen name="Main" component={MainTabs} />}
      <Stack.Screen name="Lesson" component={LessonScreen} />
      <Stack.Screen name="Vocabulary" component={VocabularyScreen} />
      <Stack.Screen name="Grammar" component={GrammarScreen} />
      <Stack.Screen name="ImageLearning" component={ImageLearningScreen} />
      <Stack.Screen name="Flashcards" component={FlashcardsScreen} />
      <Stack.Screen name="ConversationPractice" component={ConversationPracticeScreen} />
      <Stack.Screen name="SpeakingPractice" component={SpeakingPracticeScreen} />
      <Stack.Screen name="Leaderboard" component={LeaderboardScreen} options={{ headerRight: () => <Trophy color={colors.accent} /> }} />
      <Stack.Screen name="Achievements" component={AchievementsScreen} />
      <Stack.Screen name="Settings" component={SettingsScreen} options={{ headerRight: () => <Settings color={colors.accent} /> }} />
      <Stack.Screen name="Notifications" component={NotificationsScreen} options={{ headerRight: () => <Bell color={colors.accent} /> }} />
    </Stack.Navigator>
  );
}
