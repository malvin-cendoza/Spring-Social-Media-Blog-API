package com.example.exception;

/**
 * Custom exception used when a requested resource is not found in the system.
 * Typically thrown in service methods when an account or message cannot be located.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}