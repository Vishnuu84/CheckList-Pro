package com.yiu.backend.exception;

import java.time.LocalDateTime;

public class ErrorResponse {
    private LocalDateTime timestamp;
    private String message;
    private int status;

    public ErrorResponse(String message, int status) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.status = status;
    }

    // Getters
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getMessage() { return message; }
    public int getStatus() { return status; }
}