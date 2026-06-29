import hashlib
import json
from collections.abc import Awaitable, Callable
from typing import Any, TypeVar

import redis.asyncio as redis
from pydantic import BaseModel

from app.core.config import Settings
from app.core.logging import logger

T = TypeVar("T")


class Cache:
    def __init__(self, settings: Settings) -> None:
        self.settings = settings
        self.client: redis.Redis | None = None

    async def connect(self) -> None:
        try:
            self.client = redis.from_url(self.settings.redis_url, decode_responses=True)
            await self.client.ping()
            logger.info("redis_connected")
        except Exception as exc:
            self.client = None
            logger.warning("redis_unavailable", error=str(exc))

    async def close(self) -> None:
        if self.client:
            await self.client.aclose()

    async def get_or_set(
        self, namespace: str, payload: dict[str, Any], factory: Callable[[], Awaitable[T]]
    ) -> T:
        if not self.client:
            return await factory()
        key = self._key(namespace, payload)
        cached = await self.client.get(key)
        if cached:
            return json.loads(cached)
        result = await factory()
        serializable = result.model_dump(mode="json") if isinstance(result, BaseModel) else result
        await self.client.setex(key, self.settings.cache_ttl_seconds, json.dumps(serializable))
        return result

    def _key(self, namespace: str, payload: dict[str, Any]) -> str:
        digest = hashlib.sha256(json.dumps(payload, sort_keys=True).encode("utf-8")).hexdigest()
        return f"wm:ai:{namespace}:{digest}"
