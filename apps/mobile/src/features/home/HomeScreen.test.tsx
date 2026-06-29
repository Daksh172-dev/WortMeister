import React from "react";
import { render } from "@testing-library/react-native";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { HomeScreen } from "./HomeScreen";

// Reference the global jest mock directly without using the index signature on global
const mockNavigate = jest.fn();

jest.mock("@react-navigation/native", () => ({
  useNavigation: () => ({
    navigate: mockNavigate
  })
}));

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
