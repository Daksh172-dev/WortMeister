package com.wortmeister.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wortmeister.common.error.ProblemResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class RateLimitFilter extends OncePerRequestFilter {
    private static final int MAX_REQUESTS_PER_MINUTE = 120;

    private final StringRedisTemplate redis;
    private final ObjectMapper mapper;

    public RateLimitFilter(StringRedisTemplate redis, ObjectMapper mapper) {
        this.redis = redis;
        this.mapper = mapper;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/actuator") || request.getRequestURI().startsWith("/v3/api-docs");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String key = "rate:" + clientKey(request) + ":" + Instant.now().getEpochSecond() / 60;
        Long count = null;
        try {
            count = redis.opsForValue().increment(key);
            if (count != null && count == 1) {
                redis.expire(key, Duration.ofMinutes(2));
            }
        } catch (RuntimeException ignored) {
            filterChain.doFilter(request, response);
            return;
        }
        if (count > MAX_REQUESTS_PER_MINUTE) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            mapper.writeValue(response.getWriter(), new ProblemResponse(
                    "https://api.wortmeister.com/problems/rate-limit-exceeded",
                    "Too Many Requests",
                    429,
                    "RATE_LIMIT_EXCEEDED",
                    "Request rate limit exceeded.",
                    request.getHeader("X-Correlation-Id"),
                    Instant.now(),
                    List.of()
            ));
            return;
        }
        filterChain.doFilter(request, response);
    }

    private String clientKey(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.length() > 24) {
            return "token:" + authorization.substring(authorization.length() - 16);
        }
        String forwarded = request.getHeader("X-Forwarded-For");
        return forwarded != null ? forwarded.split(",")[0].trim() : request.getRemoteAddr();
    }
}
