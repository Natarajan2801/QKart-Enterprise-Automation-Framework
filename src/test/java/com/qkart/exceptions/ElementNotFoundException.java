package com.qkart.exceptions;

import org.openqa.selenium.By;

/**
 * Exception thrown when an element cannot be found within the specified timeout.
 */
public class ElementNotFoundException extends FrameworkException {

    public ElementNotFoundException(By locator, int timeoutInSeconds) {
        super(String.format("Element [%s] not found after waiting for %d seconds", locator.toString(), timeoutInSeconds));
    }

    public ElementNotFoundException(By locator, int timeoutInSeconds, Throwable cause) {
        super(String.format("Element [%s] not found after waiting for %d seconds", locator.toString(), timeoutInSeconds), cause);
    }

    public ElementNotFoundException(String message) {
        super(message);
    }
}
