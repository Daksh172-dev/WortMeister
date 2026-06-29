package com.wortmeister.admin;

import com.wortmeister.ai.AiRequestLogRepository;
import com.wortmeister.audit.AuditLogRepository;
import com.wortmeister.audit.AuditService;
import com.wortmeister.common.error.NotFoundException;
import com.wortmeister.identity.UserRepository;
import com.wortmeister.learning.Lesson;
import com.wortmeister.learning.LessonRepository;
import com.wortmeister.learning.PronunciationAttemptRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {
    private final UserRepository users;
    private final LessonRepository lessons;
    private final AiRequestLogRepository aiLogs;
    private final PronunciationAttemptRepository pronunciation;
    private final AuditLogRepository auditLogs;
    private final AuditService auditService;

    public AdminService(UserRepository users, LessonRepository lessons, AiRequestLogRepository aiLogs,
            PronunciationAttemptRepository pronunciation, AuditLogRepository auditLogs, AuditService auditService) {
        this.users = users;
        this.lessons = lessons;
        this.aiLogs = aiLogs;
        this.pronunciation = pronunciation;
        this.auditLogs = auditLogs;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public List<AdminDtos.UserAdminResponse> users() {
        return users.findAll().stream()
                .map(u -> new AdminDtos.UserAdminResponse(u.getPublicId(), u.getEmail(), u.getRole().name(),
                        u.isEmailVerified(), u.getProfile().getXp(), u.getProfile().getCoins()))
                .toList();
    }

    @Transactional
    public AdminDtos.LessonUpsertRequest createLesson(AdminDtos.LessonUpsertRequest request) {
        lessons.save(new Lesson(request.title(), request.cefrLevel(), request.category(), request.content()));
        auditService.record("admin", "LESSON_CREATED", request.title(), request.cefrLevel());
        return request;
    }

    @Transactional
    public AdminDtos.LessonUpsertRequest updateLesson(String lessonId, AdminDtos.LessonUpsertRequest request) {
        Lesson lesson = lessons.findByPublicId(lessonId).orElseThrow(() -> new NotFoundException("Lesson not found."));
        lesson.update(request.title(), request.cefrLevel(), request.category(), request.content());
        auditService.record("admin", "LESSON_UPDATED", lessonId, request.title());
        return request;
    }

    @Transactional(readOnly = true)
    public AdminDtos.AnalyticsResponse analytics() {
        return new AdminDtos.AnalyticsResponse(users.count(), lessons.count(), aiLogs.count(), pronunciation.count());
    }

    @Transactional(readOnly = true)
    public AdminDtos.DashboardResponse dashboard() {
        return new AdminDtos.DashboardResponse(analytics(), auditLogs.count(), 0, "UP");
    }

    @Transactional(readOnly = true)
    public List<AdminDtos.AuditLogResponse> auditLogs() {
        return auditLogs.findTop100ByOrderByCreatedAtDesc().stream()
                .map(log -> new AdminDtos.AuditLogResponse(log.getPublicId(), log.getActor(), log.getAction(),
                        log.getResource(), log.getMetadata()))
                .toList();
    }

    public List<AdminDtos.ReportResponse> reports() {
        return List.of(
                new AdminDtos.ReportResponse("learner-retention", "READY", "/api/admin/v1/reports/learner-retention.csv"),
                new AdminDtos.ReportResponse("ai-cost", "READY", "/api/admin/v1/reports/ai-cost.csv"));
    }
}
