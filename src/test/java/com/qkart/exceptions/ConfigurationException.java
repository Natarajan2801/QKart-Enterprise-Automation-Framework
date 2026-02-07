package com.qkart.exceptions;

/**
 * Exception thrown when there's an error loading or reading configuration.
 */
public class ConfigurationException extends FrameworkException {

    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates an exception for missing property.
     */
    public static ConfigurationException propertyNotFound(String propertyKey) {
        return new ConfigurationException(String.format("Configuration property '%s' not found or is invalid", propertyKey));
    }
}
