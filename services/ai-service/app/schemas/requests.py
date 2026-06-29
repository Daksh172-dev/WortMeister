from pydantic import BaseModel, Field, HttpUrl

from app.schemas.common import LearnerContext


class TextRequest(BaseModel):
    text: str = Field(min_length=1, max_length=8000)
    context: LearnerContext


class ImageSceneRequest(BaseModel):
    image_url: HttpUrl
    context: LearnerContext


class ContextualVocabularyRequest(BaseModel):
    words: list[str] = Field(min_length=1, max_length=50)
    context_sentence: str = Field(min_length=1, max_length=2000)
    context: LearnerContext


class ConversationRequest(BaseModel):
    scenario: str = Field(min_length=1, max_length=500)
    context: LearnerContext
    turns: int = Field(default=6, ge=2, le=20)


class GrammarCorrectionRequest(BaseModel):
    text: str = Field(min_length=1, max_length=4000)
    context: LearnerContext


class PronunciationRequest(BaseModel):
    phrase: str = Field(min_length=1, max_length=500)
    audio_url: HttpUrl | None = None
    transcript: str | None = Field(default=None, max_length=1000)
    context: LearnerContext


class LearningPathRequest(BaseModel):
    context: LearnerContext
    completed_lessons: list[str] = []
    scores: dict[str, float] = {}
    minutes_per_day: int = Field(default=15, ge=5, le=180)


class AdaptiveDifficultyRequest(BaseModel):
    context: LearnerContext
    recent_scores: list[float] = Field(min_length=1, max_length=30)
    current_difficulty: int = Field(default=3, ge=1, le=5)


class QuizGenerationRequest(BaseModel):
    topic: str = Field(min_length=1, max_length=300)
    context: LearnerContext
    question_count: int = Field(default=5, ge=1, le=20)


class FlashcardGenerationRequest(BaseModel):
    source_text: str = Field(min_length=1, max_length=8000)
    context: LearnerContext
    card_count: int = Field(default=10, ge=1, le=50)


class EmbeddingRequest(BaseModel):
    texts: list[str] = Field(min_length=1, max_length=100)


class SimilarWordRequest(BaseModel):
    word: str = Field(min_length=1, max_length=100)
    candidates: list[str] = Field(min_length=1, max_length=500)
    limit: int = Field(default=10, ge=1, le=50)


class TranslationRequest(BaseModel):
    text: str = Field(min_length=1, max_length=4000)
    source_language: str = "de"
    target_language: str = "en"


class TutorChatRequest(BaseModel):
    context: LearnerContext
    messages: list[dict[str, str]] = Field(min_length=1, max_length=20)
