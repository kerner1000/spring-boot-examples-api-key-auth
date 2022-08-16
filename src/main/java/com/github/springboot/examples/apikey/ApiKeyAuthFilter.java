package com.github.springboot.examples.apikey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private final static Logger log = LoggerFactory.getLogger(ApiKeyAuthFilter.class);

    @Value("${app.auth.api-key.auth-token-header-name}")
    private String apiKeyHeader;

    @Value("${app.auth.api-key.auth-token}")
    private String apiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String apiKeyCandidate = request.getHeader(apiKeyHeader);

        if (apiKeyCandidate == null) {
            log.error("Expected {} header, got null", apiKeyHeader);
        } else if (apiKey.equals(apiKeyCandidate)) {
            grantAccess();
            filterChain.doFilter(request, response);
            return;
        } else {
            log.error("Invalid Api-Key (got: {})", apiKeyCandidate);
        }
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

    private void grantAccess() {
        AuthenticationToken<String> token = new AuthenticationToken<>(apiKey, Collections.emptyList());
        token.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(token);
    }
}

