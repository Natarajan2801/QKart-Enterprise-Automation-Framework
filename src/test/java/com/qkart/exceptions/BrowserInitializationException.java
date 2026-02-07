package com.qkart.exceptions;

/**
 * Exception thrown when browser driver initialization fails.
 */
public class BrowserInitializationException extends FrameworkException {

    public BrowserInitializationException(String browserName) {
        super(String.format("Failed to initialize browser: %s", browserName));
    }

    public BrowserInitializationException(String browserName, Throwable cause) {
        super(String.format("Failed to initialize browser: %s", browserName), cause);
    }
}

