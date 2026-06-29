from functools import lru_cache
from typing import Literal

from pydantic import Field
from pydantic_settings import BaseSettings, SettingsConfigDict


class Settings(BaseSettings):
    model_config = SettingsConfigDict(env_file=".env", env_file_encoding="utf-8", extra="ignore")

    environment: str = "local"
    log_level: str = "INFO"
    service_name: str = "wortmeister-ai-service"

    internal_api_key: str = Field(default="local-ai-internal-key")
    allowed_clock_skew_seconds: int = 60

    ai_provider: Literal["auto", "openai", "gemini", "local"] = "auto"
    openai_api_key: str | None = None
    gemini_api_key: str | None = None
    openai_model: str = "gpt-4o-mini"
    gemini_model: str = "gemini-1.5-flash"
    whisper_model: str = "whisper-1"

    redis_url: str = "redis://localhost:6379/1"
    cache_ttl_seconds: int = 900
    request_timeout_seconds: float = 20
    max_retries: int = 2

    embedding_model_name: str = "sentence-transformers/paraphrase-multilingual-MiniLM-L12-v2"
    max_prompt_chars: int = 8000
    max_chat_turns: int = 20


@lru_cache
def get_settings() -> Settings:
    return Settings()
