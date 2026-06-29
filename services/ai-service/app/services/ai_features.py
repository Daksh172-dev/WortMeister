import re
from collections import Counter

from tenacity import AsyncRetrying, retry_if_exception_type, stop_after_attempt, wait_exponential_jitter

from app.core.config import get_settings
from app.providers.base import LlmProvider
from app.schemas.common import AiMetadata, LearnerContext
from app.schemas.requests import (
    AdaptiveDifficultyRequest,
    ContextualVocabularyRequest,
    ConversationRequest,
    FlashcardGenerationRequest,
    GrammarCorrectionRequest,
    ImageSceneRequest,
    LearningPathRequest,
    PronunciationRequest,
    QuizGenerationRequest,
    TextRequest,
    TranslationRequest,
    TutorChatRequest,
)
from app.schemas.responses import (
    AdaptiveDifficultyResponse,
    ContextualVocabularyResponse,
    ConversationResponse,
    FlashcardGenerationResponse,
    GrammarCorrectionResponse,
    ImageSceneResponse,
    LearningPathResponse,
    PronunciationResponse,
    QuizGenerationResponse,
    TranslationResponse,
    TutorChatResponse,
    VocabularyExtractionResponse,
)


GERMAN_STOPWORDS = {
    "und",
    "oder",
    "aber",
    "ich",
    "du",
    "er",
    "sie",
    "es",
    "wir",
    "ihr",
    "der",
    "die",
    "das",
    "ein",
    "eine",
    "ist",
    "bin",
    "sind",
    "zu",
    "mit",
}


class AiFeatureService:
    def __init__(self, provider: LlmProvider) -> None:
        self.provider = provider

    def metadata(self, confidence: float = 0.82) -> AiMetadata:
        return AiMetadata(provider=self.provider.name, model=self.provider.model, confidence=confidence)

    async def extract_vocabulary(self, request: TextRequest) -> VocabularyExtractionResponse:
        words = self._extract_terms(request.text)
        terms = [
            {
                "term": word,
                "translation": self._rough_translation(word),
                "level": request.context.cefr_level,
                "example": f"Ich lerne das Wort '{word}'.",
            }
            for word in words
        ]
        return VocabularyExtractionResponse(terms=terms, metadata=self.metadata())

    async def recognize_scene(self, request: ImageSceneRequest) -> ImageSceneResponse:
        prompt = f"Recognize the scene and produce German learning vocabulary for {request.context.cefr_level}."
        scene = await self._call_provider(lambda: self.provider.analyze_image(str(request.image_url), prompt))
        objects = self._extract_terms(scene)[:8] or ["Cafe", "Tisch", "Person"]
        vocabulary = [
            {"german": item, "english": self._rough_translation(item), "article": "das"} for item in objects
        ]
        return ImageSceneResponse(scene=scene, objects=objects, vocabulary=vocabulary, metadata=self.metadata())

    async def contextual_vocabulary(
        self, request: ContextualVocabularyRequest
    ) -> ContextualVocabularyResponse:
        items = [
            {
                "word": word,
                "meaning": self._rough_translation(word),
                "contextual_usage": request.context_sentence,
                "memory_hook": f"Connect '{word}' to the sentence context.",
            }
            for word in request.words
        ]
        return ContextualVocabularyResponse(items=items, metadata=self.metadata())

    async def conversation(self, request: ConversationRequest) -> ConversationResponse:
        turns = []
        for index in range(request.turns):
            speaker = "Tutor" if index % 2 == 0 else "Learner"
            text = (
                f"{speaker} turn {index + 1}: Practice {request.scenario} "
                f"at {request.context.cefr_level} level."
            )
            turns.append({"speaker": speaker, "text": text})
        return ConversationResponse(title=request.scenario, turns=turns, metadata=self.metadata())

    async def correct_grammar(self, request: GrammarCorrectionRequest) -> GrammarCorrectionResponse:
        corrected = request.text.strip()
        corrected = re.sub(r"\bich bin gehe\b", "ich gehe", corrected, flags=re.IGNORECASE)
        corrections = []
        if corrected != request.text.strip():
            corrections.append(
                {
                    "type": "verb_usage",
                    "original": request.text,
                    "correction": corrected,
                    "explanation": "German present tense usually does not use an auxiliary like English.",
                }
            )
        if not corrections:
            corrections.append({"type": "style", "original": request.text, "correction": corrected, "explanation": "No major grammar issue detected."})
        return GrammarCorrectionResponse(corrected_text=corrected, corrections=corrections, metadata=self.metadata())

    async def pronunciation(self, request: PronunciationRequest) -> PronunciationResponse:
        transcript = request.transcript or (
            await self._call_provider(lambda: self.provider.transcribe(str(request.audio_url)))
            if request.audio_url
            else request.phrase
        )
        score = self._pronunciation_score(request.phrase, transcript)
        feedback = [
            "Compare vowel length against the model phrase.",
            "Keep final consonants crisp.",
            "Repeat the phrase slowly, then at natural speed.",
        ]
        return PronunciationResponse(score=score, transcript=transcript, feedback=feedback, metadata=self.metadata())

    async def learning_path(self, request: LearningPathRequest) -> LearningPathResponse:
        weak = request.context.weaknesses or ["articles", "word order", "pronunciation"]
        path = [
            {"day": str(index + 1), "focus": focus, "activity": f"Targeted {focus} lesson and review"}
            for index, focus in enumerate(weak[:7])
        ]
        return LearningPathResponse(path=path, estimated_days=max(7, len(path)), metadata=self.metadata())

    async def adaptive_difficulty(
        self, request: AdaptiveDifficultyRequest
    ) -> AdaptiveDifficultyResponse:
        average = sum(request.recent_scores) / len(request.recent_scores)
        difficulty = request.current_difficulty
        if average > 85:
            difficulty += 1
        elif average < 60:
            difficulty -= 1
        difficulty = min(5, max(1, difficulty))
        return AdaptiveDifficultyResponse(
            recommended_difficulty=difficulty,
            reason=f"Recent average score is {average:.1f}.",
            metadata=self.metadata(),
        )

    async def quiz(self, request: QuizGenerationRequest) -> QuizGenerationResponse:
        questions = [
            {
                "type": "multiple_choice",
                "prompt": f"{request.topic}: question {index + 1}",
                "options": ["der", "die", "das", "den"],
                "answer": "der",
                "explanation": "Review article and case rules.",
            }
            for index in range(request.question_count)
        ]
        return QuizGenerationResponse(questions=questions, metadata=self.metadata())

    async def flashcards(self, request: FlashcardGenerationRequest) -> FlashcardGenerationResponse:
        words = self._extract_terms(request.source_text)[: request.card_count]
        cards = [
            {
                "front": word,
                "back": self._rough_translation(word),
                "example": f"Das Wort {word} erscheint im Kontext.",
            }
            for word in words
        ]
        return FlashcardGenerationResponse(cards=cards, metadata=self.metadata())

    async def translate(self, request: TranslationRequest) -> TranslationResponse:
        system = "Translate accurately for a German learning product. Return only the translation."
        user = f"Translate from {request.source_language} to {request.target_language}: {request.text}"
        translated = await self._call_provider(lambda: self.provider.generate(system, user))
        return TranslationResponse(translated_text=translated, metadata=self.metadata())

    async def tutor_chat(self, request: TutorChatRequest) -> TutorChatResponse:
        last = request.messages[-1].get("content", "")
        reply = await self._call_provider(
            lambda: self.provider.generate(
                self._system_for(request.context),
                f"Respond as a German tutor. Learner said: {last}",
            )
        )
        suggestions = ["Try saying it in German.", "Ask for an example.", "Practice with a short dialogue."]
        return TutorChatResponse(reply=reply, suggestions=suggestions, metadata=self.metadata())

    def _system_for(self, context: LearnerContext) -> str:
        return (
            "You are WortMeister, a precise and encouraging German tutor. "
            f"Adapt to CEFR {context.cefr_level}. Correct mistakes briefly and give usable examples."
        )

    async def _call_provider(self, operation):
        settings = get_settings()
        async for attempt in AsyncRetrying(
            stop=stop_after_attempt(settings.max_retries + 1),
            wait=wait_exponential_jitter(initial=0.5, max=4),
            retry=retry_if_exception_type((TimeoutError, ConnectionError)),
            reraise=True,
        ):
            with attempt:
                return await operation()
        raise RuntimeError("Provider retry loop exhausted")

    def _extract_terms(self, text: str) -> list[str]:
        tokens = re.findall(r"[A-Za-zÄÖÜäöüß]{3,}", text)
        counts = Counter(token.strip() for token in tokens if token.lower() not in GERMAN_STOPWORDS)
        return [word for word, _ in counts.most_common(20)]

    def _rough_translation(self, word: str) -> str:
        dictionary = {
            "Tisch": "table",
            "Lampe": "lamp",
            "Buch": "book",
            "Cafe": "cafe",
            "Person": "person",
        }
        return dictionary.get(word, f"translation of {word}")

    def _pronunciation_score(self, expected: str, transcript: str) -> float:
        expected_terms = set(self._extract_terms(expected.lower()))
        transcript_terms = set(self._extract_terms(transcript.lower()))
        if not expected_terms:
            return 60.0
        overlap = len(expected_terms & transcript_terms) / len(expected_terms)
        length_penalty = min(20, abs(len(expected) - len(transcript)) * 0.2)
        return round(max(35.0, min(99.0, 65 + overlap * 35 - length_penalty)), 2)
