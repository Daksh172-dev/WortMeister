from fastapi import Request
from fastapi.responses import JSONResponse
from pydantic import BaseModel


class AiServiceError(Exception):
    def __init__(self, status_code: int, code: str, message: str) -> None:
        super().__init__(message)
        self.status_code = status_code
        self.code = code
        self.message = message


class ProblemResponse(BaseModel):
    type: str
    title: str
    status: int
    code: str
    message: str
    correlationId: str | None = None


async def ai_error_handler(request: Request, exc: AiServiceError) -> JSONResponse:
    correlation_id = request.headers.get("X-Correlation-Id")
    body = ProblemResponse(
        type=f"https://api.wortmeister.com/problems/{exc.code.lower().replace('_', '-')}",
        title="AI service error",
        status=exc.status_code,
        code=exc.code,
        message=exc.message,
        correlationId=correlation_id,
    )
    return JSONResponse(status_code=exc.status_code, content=body.model_dump())


async def unexpected_error_handler(request: Request, exc: Exception) -> JSONResponse:
    correlation_id = request.headers.get("X-Correlation-Id")
    body = ProblemResponse(
        type="https://api.wortmeister.com/problems/internal-ai-error",
        title="AI service error",
        status=500,
        code="INTERNAL_AI_ERROR",
        message="Unexpected AI service error.",
        correlationId=correlation_id,
    )
    return JSONResponse(status_code=500, content=body.model_dump())
