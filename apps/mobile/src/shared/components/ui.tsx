import { LinearGradient } from "expo-linear-gradient";
import { LucideIcon } from "lucide-react-native";
import React, { PropsWithChildren } from "react";
import { ActivityIndicator, Pressable, Text, TextInput, View, ViewProps } from "react-native";
import Animated, { FadeInDown } from "react-native-reanimated";
import { useTheme } from "@/shared/hooks/useTheme";
import { useNetworkStore } from "@/shared/state/networkStore";
import { palette } from "@/shared/theme/tokens";

export function Screen({ children }: PropsWithChildren) {
  const { colors } = useTheme();
  return (
    <View style={{ backgroundColor: colors.background }} className="flex-1">
      <NetworkBanner />
      {children}
    </View>
  );
}

function NetworkBanner() {
  const isOnline = useNetworkStore((state) => state.isOnline);
  if (isOnline) return null;
  return (
    <View className="absolute left-0 right-0 top-0 z-50 bg-gold px-4 py-2">
      <Text className="text-center text-xs font-black uppercase text-ink">Offline mode · changes will sync later</Text>
    </View>
  );
}

export function PremiumHeader({ eyebrow, title, subtitle }: { eyebrow?: string; title: string; subtitle?: string }) {
  return (
    <Animated.View entering={FadeInDown.duration(450)} className="px-5 pb-4 pt-16">
      {eyebrow ? <Text className="mb-2 text-xs font-bold uppercase tracking-widest text-moss">{eyebrow}</Text> : null}
      <Text className="text-4xl font-black text-paper dark:text-paper">{title}</Text>
      {subtitle ? <Text className="mt-2 text-base leading-6 text-white/60">{subtitle}</Text> : null}
    </Animated.View>
  );
}

export function HeroBand({ children }: PropsWithChildren) {
  return (
    <LinearGradient colors={[palette.moss, palette.ocean, palette.plum]} start={{ x: 0, y: 0 }} end={{ x: 1, y: 1 }} className="mx-5 rounded-2xl p-5">
      {children}
    </LinearGradient>
  );
}

export function Card({ children, className = "" }: PropsWithChildren<ViewProps & { className?: string }>) {
  return <View className={`rounded-2xl border border-white/10 bg-white/10 p-4 ${className}`}>{children}</View>;
}

export function AppButton({ label, icon: Icon, onPress, tone = "primary", loading }: { label: string; icon?: LucideIcon; onPress?: () => void; tone?: "primary" | "ghost" | "danger"; loading?: boolean }) {
  const bg = tone === "primary" ? "bg-moss" : tone === "danger" ? "bg-danger" : "bg-white/10";
  return (
    <Pressable accessibilityRole="button" onPress={onPress} className={`min-h-12 flex-row items-center justify-center rounded-2xl px-4 ${bg}`}>
      {loading ? <ActivityIndicator color="white" /> : null}
      {!loading && Icon ? <Icon size={18} color="white" /> : null}
      <Text className="ml-2 font-bold text-white">{label}</Text>
    </Pressable>
  );
}

export function Field({ value, onChangeText, placeholder, secureTextEntry }: { value?: string; onChangeText: (value: string) => void; placeholder: string; secureTextEntry?: boolean }) {
  return (
    <TextInput
      value={value}
      onChangeText={onChangeText}
      placeholder={placeholder}
      placeholderTextColor="rgba(255,255,255,0.45)"
      secureTextEntry={secureTextEntry}
      className="min-h-14 rounded-2xl border border-white/10 bg-white/10 px-4 text-base text-white"
    />
  );
}

export function Skeleton({ className = "" }: { className?: string }) {
  return <View className={`overflow-hidden rounded-2xl bg-white/10 ${className}`} />;
}
