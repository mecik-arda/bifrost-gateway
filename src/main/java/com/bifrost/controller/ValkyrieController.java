package com.bifrost.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Arda Meçik
 * @version 1.0
 */
@RestController
@RequestMapping("/public/health")
public class ValkyrieController {

    @GetMapping
    public Map<String, String> checkStatus() {
        return Map.of(
                "status", "UP",
                "gateway", "Bifrost Security Gateway",
                "shield", "Active"
        );
    }
}