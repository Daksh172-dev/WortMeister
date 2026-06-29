import { useMutation } from "@tanstack/react-query";
import { Mail, Sparkles } from "lucide-react-native";
import { Controller, useForm } from "react-hook-form";
import { Text, View } from "react-native";
import { api } from "@/shared/api/client";
import { AppButton, Field, HeroBand, PremiumHeader, Screen } from "@/shared/components/ui";
import { useAuthStore } from "@/shared/state/authStore";

type Form = { email: string; password: string; displayName: string };

export function AuthScreen() {
  const setTokens = useAuthStore((state) => state.setTokens);
  const { control, handleSubmit } = useForm<Form>({ defaultValues: { email: "", password: "", displayName: "Learner" } });
  const login = useMutation({
    mutationFn: async (form: Form) => {
      try {
        return (await api.post("/auth/login", form)).data.data;
      } catch {
        return (await api.post("/auth/register", form)).data.data;
      }
    },
    onSuccess: (tokens) => setTokens(tokens.accessToken, tokens.refreshToken)
  });
  const googleLogin = useMutation({
    mutationFn: async () => {
      const idToken = process.env.EXPO_PUBLIC_GOOGLE_ID_TOKEN ?? "";
      return (await api.post("/auth/oauth/google", { idToken })).data.data;
    },
    onSuccess: (tokens) => setTokens(tokens.accessToken, tokens.refreshToken)
  });

  return (
    <Screen>
      <PremiumHeader eyebrow="WortMeister" title="German that adapts to you." subtitle="A focused, cinematic language studio for vocabulary, grammar, speaking, and AI tutoring." />
      <HeroBand>
        <View className="flex-row items-center">
          <Sparkles color="white" />
          <Text className="ml-3 flex-1 text-xl font-black text-white">Start with a premium learning path built around your pace.</Text>
        </View>
      </HeroBand>
      <View className="gap-3 px-5 pt-6">
        <Controller control={control} name="displayName" render={({ field }) => <Field placeholder="Display name" value={field.value} onChangeText={field.onChange} />} />
        <Controller control={control} name="email" render={({ field }) => <Field placeholder="Email" value={field.value} onChangeText={field.onChange} />} />
        <Controller control={control} name="password" render={({ field }) => <Field placeholder="Password" value={field.value} onChangeText={field.onChange} secureTextEntry />} />
        <AppButton icon={Mail} label="Continue" loading={login.isPending} onPress={handleSubmit((form) => login.mutate(form))} />
        <AppButton label="Continue with Google" tone="ghost" loading={googleLogin.isPending} onPress={() => googleLogin.mutate()} />
      </View>
    </Screen>
  );
}
