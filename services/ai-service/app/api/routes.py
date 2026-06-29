from fastapi import APIRouter, Depends, Request

from app.core.security import require_internal_api_key
from app.schemas.requests import (
    AdaptiveDifficultyRequest,
    ContextualVocabularyRequest,
    ConversationRequest,
    EmbeddingRequest,
    FlashcardGenerationRequest,
    GrammarCorrectionRequest,
    ImageSceneRequest,
    LearningPathRequest,
    PronunciationRequest,
    QuizGenerationRequest,
    SimilarWordRequest,
    TextRequest,
    TranslationRequest,
    TutorChatRequest,
)
from app.schemas.responses import (
    AdaptiveDifficultyResponse,
    ContextualVocabularyResponse,
    ConversationResponse,
    EmbeddingResponse,
    FlashcardGenerationResponse,
    GrammarCorrectionResponse,
    ImageSceneResponse,
    LearningPathResponse,
    PronunciationResponse,
    QuizGenerationResponse,
    SimilarWordResponse,
    TranslationResponse,
    TutorChatResponse,
    VocabularyExtractionResponse,
)
from app.services.ai_features import AiFeatureService
from app.services.embeddings import EmbeddingService

router = APIRouter(prefix="/internal/v1", dependencies=[Depends(require_internal_api_key)])


def features(request: Request) -> AiFeatureService:
    return request.app.state.features


def embeddings(request: Request) -> EmbeddingService:
    return request.app.state.embeddings


@router.post("/vocabulary/extract", response_model=VocabularyExtractionResponse)
async def extract_vocabulary(payload: TextRequest, request: Request):
    return await request.app.state.cache.get_or_set(
        "vocab_extract", payload.model_dump(mode="json"), lambda: features(request).extract_vocabulary(payload)
    )


@router.post("/images/recognize-scene", response_model=ImageSceneResponse)
async def recognize_scene(payload: ImageSceneRequest, request: Request):
    return await request.app.state.cache.get_or_set(
        "image_scene", payload.model_dump(mode="json"), lambda: features(request).recognize_scene(payload)
    )


@router.post("/vocabulary/contextual", response_model=ContextualVocabularyResponse)
async def contextual_vocabulary(payload: ContextualVocabularyRequest, request: Request):
    return await features(request).contextual_vocabulary(payload)


@router.post("/conversations/generate", response_model=ConversationResponse)
async def conversation(payload: ConversationRequest, request: Request):
    return await features(request).conversation(payload)


@router.post("/grammar/correct", response_model=GrammarCorrectionResponse)
async def grammar(payload: GrammarCorrectionRequest, request: Request):
    return await features(request).correct_grammar(payload)


@router.post("/pronunciation/score", response_model=PronunciationResponse)
async def pronunciation(payload: PronunciationRequest, request: Request):
    return await features(request).pronunciation(payload)


@router.post("/speaking/feedback", response_model=PronunciationResponse)
async def speaking_feedback(payload: PronunciationRequest, request: Request):
    return await features(request).pronunciation(payload)


@router.post("/learning/path", response_model=LearningPathResponse)
async def learning_path(payload: LearningPathRequest, request: Request):
    return await features(request).learning_path(payload)


@router.post("/learning/adaptive-difficulty", response_model=AdaptiveDifficultyResponse)
async def adaptive_difficulty(payload: AdaptiveDifficultyRequest, request: Request):
    return await features(request).adaptive_difficulty(payload)


@router.post("/quizzes/generate", response_model=QuizGenerationResponse)
async def quiz(payload: QuizGenerationRequest, request: Request):
    return await features(request).quiz(payload)


@router.post("/flashcards/generate", response_model=FlashcardGenerationResponse)
async def flashcards(payload: FlashcardGenerationRequest, request: Request):
    return await features(request).flashcards(payload)


@router.post("/embeddings", response_model=EmbeddingResponse)
async def create_embeddings(payload: EmbeddingRequest, request: Request):
    vectors = embeddings(request).embed(payload.texts)
    return EmbeddingResponse(
        vectors=vectors,
        dimensions=len(vectors[0]) if vectors else 0,
        metadata=features(request).metadata(confidence=1.0),
    )


@router.post("/words/similar", response_model=SimilarWordResponse)
async def similar_words(payload: SimilarWordRequest, request: Request):
    matches = embeddings(request).similar(payload.word, payload.candidates, payload.limit)
    return SimilarWordResponse(matches=matches, metadata=features(request).metadata(confidence=1.0))


@router.post("/translation/fallback", response_model=TranslationResponse)
async def translation(payload: TranslationRequest, request: Request):
    return await features(request).translate(payload)


@router.post("/tutor/chat", response_model=TutorChatResponse)
async def tutor_chat(payload: TutorChatRequest, request: Request):
    return await features(request).tutor_chat(payload)
