import React from "react";
import { render } from "@testing-library/react-native";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { HomeScreen } from "@/features/home/HomeScreen";

jest.mock("@react-navigation/native", () => ({ useNavigation: () => ({ navigate: jest.fn() }) }));

function renderWithClient() {
  const client = new QueryClient({ defaultOptions: { queries: { retry: false } } });
  return render(
    <QueryClientProvider client={client}>
      <HomeScreen />
    </QueryClientProvider>
  );
}

describe("HomeScreen", () => {
  it("renders the daily learning surface", () => {
    const screen = renderWithClient();
    expect(screen.getByText(/Your learning day/i)).toBeTruthy();
  });
});
