package org.yaroslaavl.userservice.dto;

import org.yaroslaavl.userservice.exception.error.ErrorType;

import java.time.LocalDateTime;

public record ErrorResponse(String message,
                            ErrorType errorType,
                            LocalDateTime timestamp,
                            String path) {

    public ErrorResponse(String message, ErrorType errorType, String path) {
        this(message, errorType, LocalDateTime.now(), path);
    }
}
