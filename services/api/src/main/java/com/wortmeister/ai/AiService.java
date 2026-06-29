package com.wortmeister.ai;

import com.wortmeister.identity.User;
import com.wortmeister.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AiService {
    private final UserService userService;
    private final AiRequestLogRepository logs;

    public AiService(UserService userService, AiRequestLogRepository logs) {
        this.userService = userService;
        this.logs = logs;
    }

    @Transactional
    public AiDtos.AiResponse personalizedLesson(Authentication authentication, AiDtos.PersonalizedLessonRequest request) {
        return respond(authentication, "PERSONALIZED_LESSON",
                "Generate lesson for " + request.cefrLevel() + " focused on " + request.goal(),
                "A structured German lesson with warm-up, grammar focus, vocabulary, practice prompts, and review.");
    }

    @Transactional
    public AiDtos.AiResponse vocabulary(Authentication authentication, AiDtos.VocabularyRecommendationRequest request) {
        return respond(authentication, "VOCABULARY_RECOMMENDATION",
                request.cefrLevel() + ":" + request.weakness(),
                "Recommended vocabulary set targeting the learner's current weakness.");
    }

    @Transactional
    public AiDtos.AiResponse pronunciation(Authentication authentication, AiDtos.PronunciationEvaluationRequest request) {
        return respond(authentication, "PRONUNCIATION_EVALUATION",
                request.phrase() + ":" + request.audioUrl(),
                "Pronunciation score with phoneme-level feedback and corrective drills.");
    }

    @Transactional
    public AiDtos.AiResponse image(Authentication authentication, AiDtos.ImageExplanationRequest request) {
        return respond(authentication, "IMAGE_EXPLANATION",
                request.imageUrl() + ":" + request.cefrLevel(),
                "Image explanation in learner-appropriate German with key vocabulary.");
    }

    @Transactional
    public AiDtos.AiResponse conversation(Authentication authentication, AiDtos.ConversationRequest request) {
        return respond(authentication, "CONVERSATION_GENERATION",
                request.cefrLevel() + ":" + request.scenario(),
                "Conversation prompt with role-play turns, hints, and corrections.");
    }

    @Transactional
    public AiDtos.AiResponse flashcards(Authentication authentication, AiDtos.FlashcardRequest request) {
        return respond(authentication, "FLASHCARD_GENERATION",
                request.cefrLevel() + ":" + request.sourceText(),
                "Generated " + Math.max(1, request.cardCount()) + " flashcards with front, back, example, and review hint.");
    }

    @Transactional
    public AiDtos.AiResponse tutorChat(Authentication authentication, AiDtos.TutorChatRequest request) {
        return respond(authentication, "AI_TUTOR_CHAT",
                request.cefrLevel() + ":" + request.message(),
                "Tutor response with correction, encouragement, and a short German example.");
    }

    private AiDtos.AiResponse respond(Authentication authentication, String feature, String prompt, String response) {
        User user = userService.currentUser(authentication);
        logs.save(new AiRequestLog(user, feature, prompt, response));
        return new AiDtos.AiResponse(feature, response);
    }
}
