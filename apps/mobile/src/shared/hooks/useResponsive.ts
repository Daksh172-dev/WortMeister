import { useWindowDimensions } from "react-native";

export function useResponsive() {
  const { width, height } = useWindowDimensions();
  return {
    width,
    height,
    isCompact: width < 380,
    isTablet: width >= 768,
    contentPadding: width >= 768 ? 32 : 20
  };
}
