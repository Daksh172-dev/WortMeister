import google.generativeai as genai

from app.providers.base import LlmProvider


class GeminiProvider(LlmProvider):
    name = "gemini"

    def __init__(self, api_key: str, model: str) -> None:
        self.model = model
        genai.configure(api_key=api_key)
        self.client = genai.GenerativeModel(model)

    async def generate(self, system: str, user: str) -> str:
        response = await self.client.generate_content_async(f"{system}\n\n{user}")
        return response.text or ""

    async def analyze_image(self, image_url: str, prompt: str) -> str:
        response = await self.client.generate_content_async(f"{prompt}\nImage URL: {image_url}")
        return response.text or ""
