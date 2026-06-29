from abc import ABC, abstractmethod


class LlmProvider(ABC):
    name: str
    model: str

    @abstractmethod
    async def generate(self, system: str, user: str) -> str:
        raise NotImplementedError

    async def analyze_image(self, image_url: str, prompt: str) -> str:
        return await self.generate("You analyze images for German learners.", f"{prompt}\nImage: {image_url}")

    async def transcribe(self, audio_url: str) -> str:
        return f"Transcript unavailable for remote audio: {audio_url}"
