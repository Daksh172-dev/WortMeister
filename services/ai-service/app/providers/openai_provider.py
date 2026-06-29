from openai import AsyncOpenAI

from app.providers.base import LlmProvider


class OpenAiProvider(LlmProvider):
    name = "openai"

    def __init__(self, api_key: str, model: str, timeout: float) -> None:
        self.model = model
        self.client = AsyncOpenAI(api_key=api_key, timeout=timeout)

    async def generate(self, system: str, user: str) -> str:
        response = await self.client.chat.completions.create(
            model=self.model,
            messages=[{"role": "system", "content": system}, {"role": "user", "content": user}],
            temperature=0.4,
        )
        return response.choices[0].message.content or ""

    async def analyze_image(self, image_url: str, prompt: str) -> str:
        response = await self.client.chat.completions.create(
            model=self.model,
            messages=[
                {"role": "system", "content": "You analyze images for German learners."},
                {
                    "role": "user",
                    "content": [
                        {"type": "text", "text": prompt},
                        {"type": "image_url", "image_url": {"url": image_url}},
                    ],
                },
            ],
            temperature=0.2,
        )
        return response.choices[0].message.content or ""
