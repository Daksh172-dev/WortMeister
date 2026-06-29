import React from "react";
import { render } from "@testing-library/react-native";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { HomeScreen } from "@/features/home/HomeScreen";

// Create a stable reference for the mock function outside the inline factory wrapper
const mockNavigate = (() => {
  try {
    return global.jest.fn();
  } catch {
    return () => {};
  }
})();

jest.mock("@react-navigation/native", () => ({
  useNavigation: () => ({
    navigate: mockNavigate
  })
}));

// Added async/await signatures to resolve the test rendering pipeline correctly
async function renderWithClient() {
  const client = new QueryClient({ defaultOptions: { queries: { retry: false } } });
  return await render(
    <QueryClientProvider client={client}>
      <HomeScreen />
    </QueryClientProvider>
  );
}

describe("HomeScreen", () => {
  it("renders the daily learning surface", async () => {
    const screen = await renderWithClient();
    expect(screen.getByText(/Your learning day/i)).toBeTruthy();
  });
});
