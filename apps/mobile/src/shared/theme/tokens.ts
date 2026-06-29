export type ThemeName = "dark" | "light";

export const palette = {
  ink: "#101113",
  ink2: "#181A1F",
  ink3: "#24272F",
  paper: "#F8F5EF",
  paper2: "#ECE6DA",
  moss: "#7FA36B",
  coral: "#EF806A",
  gold: "#EAB84C",
  ocean: "#4E8DBD",
  plum: "#8F6AAE",
  white: "#FFFFFF",
  muted: "#A7ABB6",
  danger: "#E75D61"
};

export const themes = {
  dark: {
    background: palette.ink,
    surface: palette.ink2,
    elevated: palette.ink3,
    text: palette.paper,
    muted: palette.muted,
    accent: palette.moss,
    secondary: palette.ocean,
    border: "#30343D"
  },
  light: {
    background: palette.paper,
    surface: palette.white,
    elevated: palette.paper2,
    text: palette.ink,
    muted: "#687080",
    accent: palette.moss,
    secondary: palette.ocean,
    border: "#DCD5C8"
  }
} as const;
