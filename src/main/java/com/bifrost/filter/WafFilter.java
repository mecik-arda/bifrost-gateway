package com.bifrost.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * @author Arda Meçik
 * @version 1.0
 */
@Component
public class WafFilter extends OncePerRequestFilter {

    private static final Pattern[] ATTACK_PATTERNS = {
            Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("src[\r\n]*=[\r\n]*\\'(.*?)\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
            Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("(?i)(SELECT|INSERT|UPDATE|DELETE|DROP|ALTER|UNION|WHERE|OR|AND).*?(=|--|#|/*)", Pattern.CASE_INSENSITIVE)
    };

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
            
        MultiReadHttpServletRequest wrappedRequest = new MultiReadHttpServletRequest(request);
        
        String query = wrappedRequest.getQueryString();
        String body = wrappedRequest.getBody();
        
        boolean maliciousHeader = false;
        java.util.Enumeration<String> headerNames = wrappedRequest.getHeaderNames();
        while (headerNames != null && headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (isMalicious(wrappedRequest.getHeader(headerName))) {
                maliciousHeader = true;
                break;
            }
        }

        if (isMalicious(query) || isMalicious(body) || maliciousHeader) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Security Violation\", \"message\": \"Malicious content detected by Mjolnir.\"}");
            return;
        }
        filterChain.doFilter(wrappedRequest, response);
    }

    private boolean isMalicious(String value) {
        if (value == null) return false;
        for (Pattern pattern : ATTACK_PATTERNS) {
            if (pattern.matcher(value).find()) return true;
        }
        return false;
    }
}