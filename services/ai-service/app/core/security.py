from fastapi import Header

from app.core.config import get_settings
from app.core.errors import AiServiceError


async def require_internal_api_key(x_internal_api_key: str | None = Header(default=None)) -> None:
    settings = get_settings()
    if not x_internal_api_key or x_internal_api_key != settings.internal_api_key:
        raise AiServiceError(401, "INVALID_INTERNAL_API_KEY", "Invalid internal service credentials.")
