package com.bifrost.security;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Arda Meçik
 * @version 1.0
 */
@Service
public class BruteForceService {

    private final Map<String, Integer> attemptsCache = new ConcurrentHashMap<>();
    private final int MAX_ATTEMPT = 5;

    public void loginFailed(String ip) {
        int attempts = attemptsCache.getOrDefault(ip, 0);
        attemptsCache.put(ip, attempts + 1);
    }

    public boolean isBlocked(String ip) {
        return attemptsCache.getOrDefault(ip, 0) >= MAX_ATTEMPT;
    }

    public void loginSucceeded(String ip) {
        attemptsCache.remove(ip);
    }
}