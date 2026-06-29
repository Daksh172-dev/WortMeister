package com.wortmeister.common.security;

import com.wortmeister.identity.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserRepository users;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService, UserRepository users) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.users = users;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            try {
                String subject = jwtService.subject(authorization.substring(7));
                users.findByPublicId(subject).ifPresent(user -> {
                    UserDetails details = userDetailsService.loadUserByUsername(user.getEmail());
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            details, null, details.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                });
            } catch (RuntimeException ignored) {
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }
}
