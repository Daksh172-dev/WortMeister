package com.wortmeister.ai;

import com.wortmeister.common.web.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ai")
public class AiController {
    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/lessons/personalized")
    ApiResponse<AiDtos.AiResponse> personalizedLesson(Authentication authentication,
            @Valid @RequestBody AiDtos.PersonalizedLessonRequest request) {
        return ApiResponse.of(aiService.personalizedLesson(authentication, request));
    }

    @PostMapping("/vocabulary/recommend")
    ApiResponse<AiDtos.AiResponse> vocabulary(Authentication authentication,
            @Valid @RequestBody AiDtos.VocabularyRecommendationRequest request) {
        return ApiResponse.of(aiService.vocabulary(authentication, request));
    }

    @PostMapping("/pronunciation/evaluate")
    ApiResponse<AiDtos.AiResponse> pronunciation(Authentication authentication,
            @Valid @RequestBody AiDtos.PronunciationEvaluationRequest request) {
        return ApiResponse.of(aiService.pronunciation(authentication, request));
    }

    @PostMapping("/images/explain")
    ApiResponse<AiDtos.AiResponse> image(Authentication authentication,
            @Valid @RequestBody AiDtos.ImageExplanationRequest request) {
        return ApiResponse.of(aiService.image(authentication, request));
    }

    @PostMapping("/conversations/generate")
    ApiResponse<AiDtos.AiResponse> conversation(Authentication authentication,
            @Valid @RequestBody AiDtos.ConversationRequest request) {
        return ApiResponse.of(aiService.conversation(authentication, request));
    }

    @PostMapping("/flashcards/generate")
    ApiResponse<AiDtos.AiResponse> flashcards(Authentication authentication,
            @Valid @RequestBody AiDtos.FlashcardRequest request) {
        return ApiResponse.of(aiService.flashcards(authentication, request));
    }

    @PostMapping("/tutor/chat")
    ApiResponse<AiDtos.AiResponse> tutorChat(Authentication authentication,
            @Valid @RequestBody AiDtos.TutorChatRequest request) {
        return ApiResponse.of(aiService.tutorChat(authentication, request));
    }
}
