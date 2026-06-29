from pydantic import BaseModel, Field


class LearnerContext(BaseModel):
    learner_id: str = Field(min_length=1)
    cefr_level: str = "A1"
    native_language: str = "en"
    strengths: list[str] = []
    weaknesses: list[str] = []
    recent_words: list[str] = []
    target_goal: str | None = None


class AiMetadata(BaseModel):
    provider: str
    model: str
    cached: bool = False
    confidence: float = Field(default=0.8, ge=0, le=1)
