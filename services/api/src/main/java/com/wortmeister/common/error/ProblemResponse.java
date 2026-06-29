package com.wortmeister.common.error;

import java.time.Instant;
import java.util.List;

public record ProblemResponse(
        String type,
        String title,
        int status,
        String code,
        String message,
        String correlationId,
        Instant timestamp,
        List<FieldViolation> details
) {
    public record FieldViolation(String field, String message) {
    }
}
