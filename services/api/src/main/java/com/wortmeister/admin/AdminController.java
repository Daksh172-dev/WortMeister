package com.wortmeister.admin;

import com.wortmeister.common.web.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/v1")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    ApiResponse<List<AdminDtos.UserAdminResponse>> users() {
        return ApiResponse.of(adminService.users());
    }

    @PostMapping("/lessons")
    @ResponseStatus(HttpStatus.CREATED)
    ApiResponse<AdminDtos.LessonUpsertRequest> createLesson(@Valid @RequestBody AdminDtos.LessonUpsertRequest request) {
        return ApiResponse.of(adminService.createLesson(request));
    }

    @PutMapping("/lessons/{lessonId}")
    ApiResponse<AdminDtos.LessonUpsertRequest> updateLesson(@PathVariable String lessonId,
            @Valid @RequestBody AdminDtos.LessonUpsertRequest request) {
        return ApiResponse.of(adminService.updateLesson(lessonId, request));
    }

    @GetMapping("/analytics")
    ApiResponse<AdminDtos.AnalyticsResponse> analytics() {
        return ApiResponse.of(adminService.analytics());
    }

    @GetMapping("/dashboard")
    ApiResponse<AdminDtos.DashboardResponse> dashboard() {
        return ApiResponse.of(adminService.dashboard());
    }

    @GetMapping("/audit-logs")
    ApiResponse<List<AdminDtos.AuditLogResponse>> auditLogs() {
        return ApiResponse.of(adminService.auditLogs());
    }

    @GetMapping("/reports")
    ApiResponse<List<AdminDtos.ReportResponse>> reports() {
        return ApiResponse.of(adminService.reports());
    }
}
