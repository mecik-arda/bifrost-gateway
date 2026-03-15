package com.bifrost.exception;

/**
 * @author Arda Meçik
 * @version 1.0
 */
public record ErrorResponse(
        int status,
        String error,
        String message,
        String path
) {
}