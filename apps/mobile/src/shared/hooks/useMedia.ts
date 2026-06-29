import { Audio } from "expo-av";
import * as ImagePicker from "expo-image-picker";
import { useRef, useState } from "react";

export function useImageUpload() {
  const [imageUri, setImageUri] = useState<string>();

  async function pickImage() {
    const result = await ImagePicker.launchImageLibraryAsync({
      mediaTypes: ImagePicker.MediaTypeOptions.Images,
      quality: 0.85
    });
    if (!result.canceled) {
      setImageUri(result.assets[0].uri);
    }
  }

  return { imageUri, pickImage };
}

export function useVoiceRecorder() {
  const recording = useRef<Audio.Recording>();
  const [uri, setUri] = useState<string>();
  const [isRecording, setRecording] = useState(false);

  async function start() {
    await Audio.requestPermissionsAsync();
    await Audio.setAudioModeAsync({ allowsRecordingIOS: true, playsInSilentModeIOS: true });
    const created = await Audio.Recording.createAsync(Audio.RecordingOptionsPresets.HIGH_QUALITY);
    recording.current = created.recording;
    setRecording(true);
  }

  async function stop() {
    if (!recording.current) return;
    await recording.current.stopAndUnloadAsync();
    setUri(recording.current.getURI() ?? undefined);
    setRecording(false);
  }

  async function play() {
    if (!uri) return;
    const { sound } = await Audio.Sound.createAsync({ uri });
    await sound.playAsync();
  }

  return { uri, isRecording, start, stop, play };
}
