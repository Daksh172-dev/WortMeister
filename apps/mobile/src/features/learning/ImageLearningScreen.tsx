import { useMutation } from "@tanstack/react-query";
import { ImagePlus } from "lucide-react-native";
import { Text, View } from "react-native";
import { api } from "@/shared/api/client";
import { AppButton, Card, PremiumHeader, Screen } from "@/shared/components/ui";
import { useImageUpload } from "@/shared/hooks/useMedia";

export function ImageLearningScreen() {
  const { imageUri, pickImage } = useImageUpload();
  const explain = useMutation({
    mutationFn: async () => (await api.post("/ai/images/explain", { imageUrl: imageUri, cefrLevel: "A1" })).data.data
  });
  return (
    <Screen>
      <PremiumHeader eyebrow="Visual" title="Image Learning" subtitle="Turn real scenes into German vocabulary and explanations." />
      <View className="gap-3 px-5">
        <AppButton icon={ImagePlus} label="Upload image" onPress={pickImage} />
        <AppButton label="Explain scene" tone="ghost" onPress={() => explain.mutate()} />
        <Card>
          <Text className="font-black text-paper">{imageUri ? "Image ready" : "No image selected"}</Text>
          <Text className="mt-2 text-white/60">{explain.data?.result ?? "Upload a scene to extract useful words."}</Text>
        </Card>
      </View>
    </Screen>
  );
}
