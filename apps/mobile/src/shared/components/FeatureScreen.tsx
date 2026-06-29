import { FlatList, Text, View } from "react-native";
import { Search, Sparkles } from "lucide-react-native";
import { useMemo, useState } from "react";
import { useInfiniteQuery, useQuery } from "@tanstack/react-query";
import { api } from "@/shared/api/client";
import { AppButton, Card, Field, HeroBand, PremiumHeader, Screen, Skeleton } from "@/shared/components/ui";

type FeatureScreenProps = {
  title: string;
  eyebrow: string;
  subtitle: string;
  endpoint?: string;
  accent?: string;
  actions?: { label: string; onPress: () => void }[];
  searchable?: boolean;
  paginated?: boolean;
};

export function FeatureScreen({ title, eyebrow, subtitle, endpoint, actions = [], searchable, paginated }: FeatureScreenProps) {
  const [search, setSearch] = useState("");
  const query = useQuery({
    queryKey: ["feature", endpoint],
    queryFn: async () => (endpoint ? (await api.get(endpoint)).data.data : []),
    enabled: Boolean(endpoint) && !paginated
  });
  const infinite = useInfiniteQuery({
    queryKey: ["feature-page", endpoint, search],
    initialPageParam: 0,
    queryFn: async ({ pageParam }) => {
      if (!endpoint) return [];
      const response = await api.get(endpoint, { params: { page: pageParam, q: search } });
      return response.data.data ?? [];
    },
    getNextPageParam: (_last, pages) => (pages.length < 5 ? pages.length : undefined),
    enabled: Boolean(endpoint) && Boolean(paginated)
  });

  const items = useMemo(() => {
    const raw = paginated ? infinite.data?.pages.flat() : query.data;
    return Array.isArray(raw) ? raw : [];
  }, [infinite.data, paginated, query.data]);

  const loading = query.isLoading || infinite.isLoading;
  const error = query.error || infinite.error;

  return (
    <Screen>
      <PremiumHeader eyebrow={eyebrow} title={title} subtitle={subtitle} />
      <HeroBand>
        <View className="flex-row items-center">
          <Sparkles color="white" size={24} />
          <Text className="ml-3 flex-1 text-lg font-black text-white">Your next best German move is ready.</Text>
        </View>
      </HeroBand>
      {searchable ? (
        <View className="px-5 pt-4">
          <View className="flex-row items-center rounded-2xl bg-white/10 px-3">
            <Search size={18} color="white" />
            <View className="ml-2 flex-1">
              <Field value={search} onChangeText={setSearch} placeholder="Search German, English, grammar..." />
            </View>
          </View>
        </View>
      ) : null}
      <View className="flex-row flex-wrap gap-3 px-5 pt-4">
        {actions.map((action) => (
          <AppButton key={action.label} label={action.label} onPress={action.onPress} tone="ghost" />
        ))}
      </View>
      {loading ? (
        <View className="gap-3 px-5 pt-5">
          <Skeleton className="h-24" />
          <Skeleton className="h-24" />
          <Skeleton className="h-24" />
        </View>
      ) : error ? (
        <View className="px-5 pt-5">
          <Card>
            <Text className="font-bold text-paper">Could not load this view.</Text>
            <Text className="mt-2 text-white/60">Your cached learning data remains available offline.</Text>
          </Card>
        </View>
      ) : (
        <FlatList
          data={items.length ? items : [{ id: "local", title, content: subtitle }]}
          keyExtractor={(item, index) => String(item.id ?? item.publicId ?? index)}
          contentContainerStyle={{ padding: 20, gap: 12, paddingBottom: 120 }}
          onEndReached={() => paginated && infinite.fetchNextPage()}
          renderItem={({ item }) => (
            <Card>
              <Text className="text-lg font-black text-paper">{item.title ?? item.german ?? item.name ?? title}</Text>
              <Text className="mt-2 leading-6 text-white/60">{item.content ?? item.explanation ?? item.description ?? item.english ?? subtitle}</Text>
            </Card>
          )}
        />
      )}
    </Screen>
  );
}
