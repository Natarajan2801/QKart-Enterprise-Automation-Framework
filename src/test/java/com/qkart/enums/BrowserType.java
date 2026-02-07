package com.qkart.enums;

/**
 * Enum representing supported browser types.
 * Provides type-safe browser selection instead of string-based configuration.
 */
public enum BrowserType {
    CHROME("chrome"),
    FIREFOX("firefox"),
    EDGE("edge");

    private final String browserName;

    BrowserType(String browserName) {
        this.browserName = browserName;
    }

    public String getBrowserName() {
        return browserName;
    }

    /**
     * Converts string to BrowserType enum.
     * @param browserName The browser name string
     * @return The corresponding BrowserType enum value
     */
    public static BrowserType fromString(String browserName) {
        for (BrowserType type : BrowserType.values()) {
            if (type.browserName.equalsIgnoreCase(browserName)) {
                return type;
            }
        }
        return CHROME; // Default to Chrome
    }
}

