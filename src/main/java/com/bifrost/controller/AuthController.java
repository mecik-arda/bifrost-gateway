package com.bifrost.controller;

import com.bifrost.security.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Arda Meçik
 * @version 1.0
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/login")
    public Map<String, String> login(@RequestParam String user) {
        String token = jwtUtil.generateToken(user);
        return Map.of("token", token);
    }
}