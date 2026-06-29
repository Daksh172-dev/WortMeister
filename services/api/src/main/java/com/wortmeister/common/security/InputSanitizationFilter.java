package com.wortmeister.common.security;

import com.wortmeister.common.error.ApiException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class InputSanitizationFilter extends OncePerRequestFilter {
    private static final Pattern DANGEROUS_QUERY = Pattern.compile("(?i)(<script|javascript:|union\\s+select|drop\\s+table|insert\\s+into|--\\s*$)");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String query = request.getQueryString();
        if (query != null && DANGEROUS_QUERY.matcher(query).find()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "UNSAFE_INPUT", "Request contains unsafe input.");
        }
        filterChain.doFilter(request, response);
    }
}
