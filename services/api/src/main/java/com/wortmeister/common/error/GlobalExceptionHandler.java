package com.wortmeister.common.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    ResponseEntity<ProblemResponse> api(ApiException ex, HttpServletRequest request) {
        return build(ex.status(), ex.code(), ex.getMessage(), request, List.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ProblemResponse> validation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<ProblemResponse.FieldViolation> details = ex.getBindingResult().getFieldErrors().stream()
                .map(this::toViolation)
                .toList();
        return build(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "One or more fields are invalid.", request, details);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ProblemResponse> constraint(ConstraintViolationException ex, HttpServletRequest request) {
        List<ProblemResponse.FieldViolation> details = ex.getConstraintViolations().stream()
                .map(v -> new ProblemResponse.FieldViolation(v.getPropertyPath().toString(), v.getMessage()))
                .toList();
        return build(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "One or more fields are invalid.", request, details);
    }

    @ExceptionHandler(AuthenticationException.class)
    ResponseEntity<ProblemResponse> auth(AuthenticationException ex, HttpServletRequest request) {
        return build(HttpStatus.UNAUTHORIZED, "AUTHENTICATION_FAILED", "Authentication failed.", request, List.of());
    }

    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ProblemResponse> denied(AccessDeniedException ex, HttpServletRequest request) {
        return build(HttpStatus.FORBIDDEN, "ACCESS_DENIED", "You do not have permission to perform this action.", request, List.of());
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ProblemResponse> unexpected(Exception ex, HttpServletRequest request) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Unexpected server error.", request, List.of());
    }

    private ProblemResponse.FieldViolation toViolation(FieldError error) {
        return new ProblemResponse.FieldViolation(error.getField(), error.getDefaultMessage());
    }

    private ResponseEntity<ProblemResponse> build(HttpStatus status, String code, String message,
            HttpServletRequest request, List<ProblemResponse.FieldViolation> details) {
        String correlationId = request.getHeader("X-Correlation-Id");
        ProblemResponse body = new ProblemResponse(
                "https://api.wortmeister.com/problems/" + code.toLowerCase().replace('_', '-'),
                status.getReasonPhrase(),
                status.value(),
                code,
                message,
                correlationId,
                Instant.now(),
                details
        );
        return ResponseEntity.status(status).body(body);
    }
}
