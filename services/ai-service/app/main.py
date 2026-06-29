from contextlib import asynccontextmanager
from time import perf_counter

from fastapi import FastAPI, Request
from fastapi.responses import JSONResponse
import structlog

from app.api.routes import router
from app.core.cache import Cache
from app.core.config import get_settings
from app.core.errors import AiServiceError, ai_error_handler, unexpected_error_handler
from app.core.logging import configure_logging, logger
from app.providers.factory import create_provider
from app.services.ai_features import AiFeatureService
from app.services.embeddings import EmbeddingService


@asynccontextmanager
async def lifespan(app: FastAPI):
    settings = get_settings()
    configure_logging(settings.log_level)
    provider = create_provider(settings)
    cache = Cache(settings)
    await cache.connect()
    app.state.settings = settings
    app.state.provider = provider
    app.state.features = AiFeatureService(provider)
    app.state.embeddings = EmbeddingService(settings.embedding_model_name)
    app.state.cache = cache
    logger.info("ai_service_started", provider=provider.name, model=provider.model)
    try:
        yield
    finally:
        await cache.close()
        logger.info("ai_service_stopped")


app = FastAPI(
    title="WortMeister AI Service",
    version="v1",
    description="Internal AI microservice for German learning intelligence.",
    lifespan=lifespan,
)

app.add_exception_handler(AiServiceError, ai_error_handler)
app.add_exception_handler(Exception, unexpected_error_handler)


@app.middleware("http")
async def request_logging(request: Request, call_next):
    start = perf_counter()
    correlation_id = request.headers.get("X-Correlation-Id")
    structlog.contextvars.bind_contextvars(correlation_id=correlation_id, path=request.url.path)
    try:
        response = await call_next(request)
    finally:
        structlog.contextvars.clear_contextvars()
    latency_ms = round((perf_counter() - start) * 1000, 2)
    logger.info(
        "request_completed",
        method=request.method,
        path=request.url.path,
        status_code=response.status_code,
        latency_ms=latency_ms,
    )
    return response


@app.get("/health")
async def health() -> JSONResponse:
    return JSONResponse({"status": "UP", "service": "wortmeister-ai-service"})


app.include_router(router)
