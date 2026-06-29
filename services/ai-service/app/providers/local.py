from app.providers.base import LlmProvider


class LocalDeterministicProvider(LlmProvider):
    name = "local"
    model = "deterministic"

    async def generate(self, system: str, user: str) -> str:
        del system
        compact = " ".join(user.split())[:600]
        return f"Generated learning response based on: {compact}"

    async def analyze_image(self, image_url: str, prompt: str) -> str:
        del prompt
        return f"Scene appears to contain everyday objects useful for German practice. Source: {image_url}"

    async def transcribe(self, audio_url: str) -> str:
        return f"Simulated transcript for {audio_url}"
