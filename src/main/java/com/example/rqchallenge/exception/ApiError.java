package com.example.rqchallenge.exception;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
@NoArgsConstructor
public class ApiError {
    private HttpStatus status;
    private Instant timestamp;
    private String message;
    private String debugMessage;
    private ErrorType errorType;

    public ApiError(HttpStatus status, String message, ErrorType errorType, Throwable throwable) {
        this.timestamp = Instant.now();
        this.status = status;
        this.message = message;
        this.errorType = errorType;
        this.debugMessage = throwable.getLocalizedMessage();
    }
}
