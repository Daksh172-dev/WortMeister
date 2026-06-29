import React from "react";
import { Text, View } from "react-native";

type State = { hasError: boolean };

export class ErrorBoundary extends React.Component<React.PropsWithChildren, State> {
  state: State = { hasError: false };

  static getDerivedStateFromError() {
    return { hasError: true };
  }

  render() {
    if (this.state.hasError) {
      return (
        <View className="flex-1 items-center justify-center bg-ink px-6">
          <Text className="text-center text-2xl font-bold text-paper">Something went sideways.</Text>
          <Text className="mt-3 text-center text-base text-white/60">Restart WortMeister and your progress will sync.</Text>
        </View>
      );
    }
    return this.props.children;
  }
}
