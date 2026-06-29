package com.wortmeister.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class AdminDtos {
    private AdminDtos() {
    }

    public record UserAdminResponse(String id, String email, String role, boolean emailVerified, int xp, int coins) {
    }

    public record LessonUpsertRequest(@NotBlank @Size(max = 180) String title, @NotBlank String cefrLevel,
                                      @NotBlank String category, @NotBlank String content) {
    }

    public record AnalyticsResponse(long users, long lessons, long aiRequests, long pronunciationAttempts) {
    }

    public record DashboardResponse(AnalyticsResponse analytics, long auditEvents, long activeJobs, String systemHealth) {
    }

    public record AuditLogResponse(String id, String actor, String action, String resource, String metadata) {
    }

    public record ReportResponse(String name, String status, String downloadUrl) {
    }
}
