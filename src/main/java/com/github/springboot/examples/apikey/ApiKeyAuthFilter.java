package com.github.springboot.examples.apikey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private final static Logger log = LoggerFactory.getLogger(ApiKeyAuthFilter.class);

    private final String apiKeyHeader;

    private final String apiKey;

    public ApiKeyAuthFilter(String apiKeyHeader, String apiKey) {
        this.apiKeyHeader = apiKeyHeader;
        this.apiKey = apiKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String apiKeyCandidate = request.getHeader(apiKeyHeader);

        if (apiKeyCandidate == null) {
            log.error("Expected {} header, got null", apiKeyHeader);
        } else if (apiKey.equals(apiKeyCandidate)) {
            grantAccess();
        } else {
            log.error("Invalid Api-Key (got: {})", apiKeyCandidate);
        }

        filterChain.doFilter(request, response);
    }

    private void grantAccess() {
        AuthenticationToken<String> token = new AuthenticationToken<>(apiKey, Collections.emptyList());
        token.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(token);
    }
}

