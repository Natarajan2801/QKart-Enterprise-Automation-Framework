package com.qkart.exceptions;

/**
 * Base exception class for all framework-specific exceptions.
 * Provides a common parent for all custom exceptions in the framework.
 */
public class FrameworkException extends RuntimeException {

    public FrameworkException(String message) {
        super(message);
    }

    public FrameworkException(String message, Throwable cause) {
        super(message, cause);
    }
}

