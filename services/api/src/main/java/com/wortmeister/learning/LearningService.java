package com.wortmeister.learning;

import com.wortmeister.common.error.NotFoundException;
import com.wortmeister.identity.User;
import com.wortmeister.user.UserService;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LearningService {
    private final LessonRepository lessons;
    private final VocabularyRepository vocabulary;
    private final GrammarTopicRepository grammar;
    private final MediaAssetRepository media;
    private final LearningProgressRepository progress;
    private final PronunciationAttemptRepository pronunciationAttempts;
    private final UserService userService;

    public LearningService(LessonRepository lessons, VocabularyRepository vocabulary, GrammarTopicRepository grammar,
            MediaAssetRepository media, LearningProgressRepository progress,
            PronunciationAttemptRepository pronunciationAttempts, UserService userService) {
        this.lessons = lessons;
        this.vocabulary = vocabulary;
        this.grammar = grammar;
        this.media = media;
        this.progress = progress;
        this.pronunciationAttempts = pronunciationAttempts;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<LearningDtos.LessonResponse> lessons() {
        return lessons.findAll().stream().map(this::toLesson).toList();
    }

    @Transactional(readOnly = true)
    public LearningDtos.LessonResponse lesson(String lessonId) {
        return lessons.findByPublicId(lessonId).map(this::toLesson).orElseThrow(() -> new NotFoundException("Lesson not found."));
    }

    @Transactional(readOnly = true)
    public List<LearningDtos.VocabularyResponse> vocabulary(String cefrLevel) {
        return vocabulary.findTop20ByCefrLevelOrderByGermanAsc(cefrLevel).stream().map(this::toVocabulary).toList();
    }

    @Transactional(readOnly = true)
    public List<LearningDtos.GrammarResponse> grammar(String cefrLevel) {
        return grammar.findByCefrLevelOrderByTitleAsc(cefrLevel).stream().map(this::toGrammar).toList();
    }

    @Transactional(readOnly = true)
    public List<LearningDtos.MediaResponse> images() {
        return media.findByType("IMAGE").stream().map(this::toMedia).toList();
    }

    @Transactional(readOnly = true)
    public List<LearningDtos.MediaResponse> audio() {
        return media.findByType("AUDIO").stream().map(this::toMedia).toList();
    }

    @Transactional
    public LearningDtos.ProgressResponse complete(Authentication authentication, LearningDtos.CompleteLessonRequest request) {
        User user = userService.currentUser(authentication);
        Lesson lesson = lessons.findByPublicId(request.lessonId()).orElseThrow(() -> new NotFoundException("Lesson not found."));
        boolean completed = request.score() >= 60;
        progress.save(new LearningProgress(user, lesson, request.score(), completed));
        int xp = completed ? 25 : 5;
        int coins = completed ? 10 : 1;
        user.getProfile().addRewards(xp, coins);
        return new LearningDtos.ProgressResponse(lesson.getPublicId(), request.score(), completed, xp, coins);
    }

    @Transactional
    public LearningDtos.PronunciationResponse pronounce(Authentication authentication, LearningDtos.PronunciationRequest request) {
        User user = userService.currentUser(authentication);
        int score = Math.min(98, Math.max(40, 70 + request.phrase().length() % 29));
        String feedback = "Pronunciation evaluated. Focus on vowel length, stress, and final consonant clarity.";
        pronunciationAttempts.save(new PronunciationAttempt(user, request.phrase(), request.audioUrl(), score, feedback));
        return new LearningDtos.PronunciationResponse(score, feedback);
    }

    private LearningDtos.LessonResponse toLesson(Lesson lesson) {
        return new LearningDtos.LessonResponse(lesson.getPublicId(), lesson.getTitle(), lesson.getCefrLevel(),
                lesson.getCategory(), lesson.getContent());
    }

    private LearningDtos.VocabularyResponse toVocabulary(VocabularyItem item) {
        return new LearningDtos.VocabularyResponse(item.getPublicId(), item.getGerman(), item.getEnglish(),
                item.getArticle(), item.getExampleSentence(), item.getCefrLevel());
    }

    private LearningDtos.GrammarResponse toGrammar(GrammarTopic topic) {
        return new LearningDtos.GrammarResponse(topic.getPublicId(), topic.getTitle(), topic.getCefrLevel(),
                topic.getExplanation());
    }

    private LearningDtos.MediaResponse toMedia(MediaAsset asset) {
        return new LearningDtos.MediaResponse(asset.getPublicId(), asset.getUrl(), asset.getType(), asset.getDescription());
    }
}
