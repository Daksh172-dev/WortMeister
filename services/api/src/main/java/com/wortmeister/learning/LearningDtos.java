package com.wortmeister.learning;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public final class LearningDtos {
    private LearningDtos() {
    }

    public record LessonResponse(String id, String title, String cefrLevel, String category, String content) {
    }

    public record VocabularyResponse(String id, String german, String english, String article, String exampleSentence,
                                     String cefrLevel) {
    }

    public record GrammarResponse(String id, String title, String cefrLevel, String explanation) {
    }

    public record MediaResponse(String id, String url, String type, String description) {
    }

    public record CompleteLessonRequest(@NotBlank String lessonId, @Min(0) @Max(100) int score) {
    }

    public record ProgressResponse(String lessonId, int score, boolean completed, int earnedXp, int earnedCoins) {
    }

    public record PronunciationRequest(@NotBlank String phrase, @NotBlank String audioUrl) {
    }

    public record PronunciationResponse(int score, String feedback) {
    }
}
