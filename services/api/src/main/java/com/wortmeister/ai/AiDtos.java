package com.wortmeister.ai;

import jakarta.validation.constraints.NotBlank;

public final class AiDtos {
    private AiDtos() {
    }

    public record PersonalizedLessonRequest(@NotBlank String cefrLevel, @NotBlank String goal) {
    }

    public record VocabularyRecommendationRequest(@NotBlank String cefrLevel, @NotBlank String weakness) {
    }

    public record PronunciationEvaluationRequest(@NotBlank String phrase, @NotBlank String audioUrl) {
    }

    public record ImageExplanationRequest(@NotBlank String imageUrl, @NotBlank String cefrLevel) {
    }

    public record ConversationRequest(@NotBlank String cefrLevel, @NotBlank String scenario) {
    }

    public record FlashcardRequest(@NotBlank String sourceText, @NotBlank String cefrLevel, int cardCount) {
    }

    public record TutorChatRequest(@NotBlank String message, @NotBlank String cefrLevel) {
    }

    public record AiResponse(String feature, String result) {
    }
}
