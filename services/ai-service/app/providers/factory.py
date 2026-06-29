from app.core.config import Settings
from app.providers.base import LlmProvider
from app.providers.gemini_provider import GeminiProvider
from app.providers.local import LocalDeterministicProvider
from app.providers.openai_provider import OpenAiProvider


def create_provider(settings: Settings) -> LlmProvider:
    if settings.ai_provider in ("openai", "auto") and settings.openai_api_key:
        return OpenAiProvider(settings.openai_api_key, settings.openai_model, settings.request_timeout_seconds)
    if settings.ai_provider in ("gemini", "auto") and settings.gemini_api_key:
        return GeminiProvider(settings.gemini_api_key, settings.gemini_model)
    return LocalDeterministicProvider()
