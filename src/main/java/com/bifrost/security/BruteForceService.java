package com.bifrost.security;

import org.springframework.stereotype.Service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;

/**
 * @author Arda Meçik
 * @version 1.0
 */
@Service
public class BruteForceService {

    private final Cache<String, Integer> attemptsCache = Caffeine.newBuilder()
            .expireAfterWrite(15, TimeUnit.MINUTES)
            .maximumSize(10000)
            .build();
            
    private final int MAX_ATTEMPT = 5;

    public void loginFailed(String ip) {
        int attempts = attemptsCache.getIfPresent(ip) == null ? 0 : attemptsCache.getIfPresent(ip);
        attemptsCache.put(ip, attempts + 1);
    }

    public boolean isBlocked(String ip) {
        return attemptsCache.getIfPresent(ip) != null && attemptsCache.getIfPresent(ip) >= MAX_ATTEMPT;
    }

    public void loginSucceeded(String ip) {
        attemptsCache.invalidate(ip);
    }
}