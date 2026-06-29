import * as NativeMMKV from "react-native-mmkv";

export const mmkv = new NativeMMKV.MMKV({ id: "wortmeister" });

export const appStorage = {
  getItem: (name: string) => {
    const value = mmkv.getString(name);
    return value ?? null;
  },
  setItem: (name: string, value: string) => mmkv.set(name, value),
  removeItem: (name: string) => mmkv.delete(name)
};
