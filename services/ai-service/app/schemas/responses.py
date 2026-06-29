from pydantic import BaseModel

from app.schemas.common import AiMetadata


class VocabularyExtractionResponse(BaseModel):
    terms: list[dict[str, str]]
    metadata: AiMetadata


class ImageSceneResponse(BaseModel):
    scene: str
    objects: list[str]
    vocabulary: list[dict[str, str]]
    metadata: AiMetadata


class ContextualVocabularyResponse(BaseModel):
    items: list[dict[str, str]]
    metadata: AiMetadata


class ConversationResponse(BaseModel):
    title: str
    turns: list[dict[str, str]]
    metadata: AiMetadata


class GrammarCorrectionResponse(BaseModel):
    corrected_text: str
    corrections: list[dict[str, str]]
    metadata: AiMetadata


class PronunciationResponse(BaseModel):
    score: float
    transcript: str
    feedback: list[str]
    metadata: AiMetadata


class LearningPathResponse(BaseModel):
    path: list[dict[str, str]]
    estimated_days: int
    metadata: AiMetadata


class AdaptiveDifficultyResponse(BaseModel):
    recommended_difficulty: int
    reason: str
    metadata: AiMetadata


class QuizGenerationResponse(BaseModel):
    questions: list[dict[str, object]]
    metadata: AiMetadata


class FlashcardGenerationResponse(BaseModel):
    cards: list[dict[str, str]]
    metadata: AiMetadata


class EmbeddingResponse(BaseModel):
    vectors: list[list[float]]
    dimensions: int
    metadata: AiMetadata


class SimilarWordResponse(BaseModel):
    matches: list[dict[str, float | str]]
    metadata: AiMetadata


class TranslationResponse(BaseModel):
    translated_text: str
    metadata: AiMetadata


class TutorChatResponse(BaseModel):
    reply: str
    suggestions: list[str]
    metadata: AiMetadata
