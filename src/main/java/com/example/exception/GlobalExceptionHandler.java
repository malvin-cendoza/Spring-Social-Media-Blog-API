package com.example.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Handles exceptions thrown across the whole application and provides meaningful HTTP responses.
 * This makes the API cleaner and easier to debug.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles cases where a specific resource was not found (e.g., account or message).
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    /**
     * Handles any other unexpected exceptions with a generic 500 error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(500).body("Something went wrong: " + ex.getMessage());
    }
}