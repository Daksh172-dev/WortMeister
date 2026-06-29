package com.wortmeister.learning;

import com.wortmeister.common.web.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/learning")
public class LearningController {
    private final LearningService learningService;

    public LearningController(LearningService learningService) {
        this.learningService = learningService;
    }

    @GetMapping("/lessons")
    ApiResponse<List<LearningDtos.LessonResponse>> lessons() {
        return ApiResponse.of(learningService.lessons());
    }

    @GetMapping("/lessons/{lessonId}")
    ApiResponse<LearningDtos.LessonResponse> lesson(@PathVariable String lessonId) {
        return ApiResponse.of(learningService.lesson(lessonId));
    }

    @PostMapping("/lessons/complete")
    ApiResponse<LearningDtos.ProgressResponse> complete(Authentication authentication,
            @Valid @RequestBody LearningDtos.CompleteLessonRequest request) {
        return ApiResponse.of(learningService.complete(authentication, request));
    }

    @GetMapping("/vocabulary")
    ApiResponse<List<LearningDtos.VocabularyResponse>> vocabulary(@RequestParam(defaultValue = "A1") String cefrLevel) {
        return ApiResponse.of(learningService.vocabulary(cefrLevel));
    }

    @GetMapping("/grammar")
    ApiResponse<List<LearningDtos.GrammarResponse>> grammar(@RequestParam(defaultValue = "A1") String cefrLevel) {
        return ApiResponse.of(learningService.grammar(cefrLevel));
    }

    @GetMapping("/images")
    ApiResponse<List<LearningDtos.MediaResponse>> images() {
        return ApiResponse.of(learningService.images());
    }

    @GetMapping("/audio")
    ApiResponse<List<LearningDtos.MediaResponse>> audio() {
        return ApiResponse.of(learningService.audio());
    }

    @PostMapping("/pronunciation/evaluate")
    ApiResponse<LearningDtos.PronunciationResponse> pronunciation(Authentication authentication,
            @Valid @RequestBody LearningDtos.PronunciationRequest request) {
        return ApiResponse.of(learningService.pronounce(authentication, request));
    }
}
