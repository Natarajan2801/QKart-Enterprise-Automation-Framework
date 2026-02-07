package com.qkart.config;

import com.qkart.enums.BrowserType;
import com.qkart.exceptions.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Singleton configuration manager that provides centralized access to all framework configurations.
 * Loads configuration from config.properties file.
 */
public final class ConfigManager {
    private static final Logger log = LogManager.getLogger(ConfigManager.class);
    private static Properties properties;
    private static final String CONFIG_PATH = System.getProperty("user.dir") + "/src/test/resources/config.properties";

    private ConfigManager() {
        // Private constructor to prevent instantiation
    }

    /**
     * Initialize configuration from config.properties.
     */
    private static synchronized void initConfig() {
        if (properties == null) {
            properties = new Properties();
            log.info("Loading configuration from: {}", CONFIG_PATH);

            try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
                properties.load(fis);
            } catch (IOException e) {
                throw new ConfigurationException("Failed to load configuration: " + e.getMessage());
            }

            // Override with system properties if provided (command line args)
            overrideWithSystemProperties();
            validateConfig();
        }
    }

    /**
     * Override config values with system properties (command line args).
     */
    private static void overrideWithSystemProperties() {
        String[] overridableProps = {"browser", "headless", "url"};
        for (String prop : overridableProps) {
            String sysValue = System.getProperty(prop);
            if (sysValue != null && !sysValue.isEmpty()) {
                properties.setProperty(prop, sysValue);
                log.info("Overriding {} with system property value: {}", prop, sysValue);
            }
        }
    }

    /**
     * Validates that essential configuration values are present.
     */
    private static void validateConfig() {
        String url = properties.getProperty("url");
        if (url == null || url.isEmpty()) {
            throw new ConfigurationException("Application URL is not configured. Please check config.properties");
        }
        log.info("Configuration loaded - URL: {}, Browser: {}, Headless: {}",
                url, properties.getProperty("browser"), properties.getProperty("headless"));
    }

    /**
     * Get a property value.
     */
    public static String get(String key) {
        if (properties == null) initConfig();
        return properties.getProperty(key);
    }

    /**
     * Get the URL for the application.
     */
    public static String getUrl() {
        if (properties == null) initConfig();
        return properties.getProperty("url");
    }

    /**
     * Get the browser type.
     */
    public static BrowserType getBrowser() {
        if (properties == null) initConfig();
        return BrowserType.fromString(properties.getProperty("browser", "chrome"));
    }

    /**
     * Check if headless mode is enabled.
     */
    public static boolean isHeadless() {
        if (properties == null) initConfig();
        return Boolean.parseBoolean(properties.getProperty("headless", "false"));
    }

    /**
     * Get implicit wait timeout in seconds.
     */
    public static int getImplicitWait() {
        if (properties == null) initConfig();
        return Integer.parseInt(properties.getProperty("implicitWait", "10"));
    }

    /**
     * Get explicit wait timeout in seconds.
     */
    public static int getExplicitWait() {
        if (properties == null) initConfig();
        return Integer.parseInt(properties.getProperty("explicitWait", "15"));
    }

    /**
     * Get page load timeout in seconds.
     */
    public static int getPageLoadTimeout() {
        if (properties == null) initConfig();
        return Integer.parseInt(properties.getProperty("pageLoadTimeout", "30"));
    }

    /**
     * Get retry count for failed tests.
     */
    public static int getRetryCount() {
        if (properties == null) initConfig();
        return Integer.parseInt(properties.getProperty("retryCount", "1"));
    }

    /**
     * Check if screenshots should be taken on failure.
     */
    public static boolean shouldTakeScreenshotOnFailure() {
        if (properties == null) initConfig();
        return Boolean.parseBoolean(properties.getProperty("screenshotOnFailure", "true"));
    }

    /**
     * Check if element highlighting is enabled.
     */
    public static boolean shouldHighlightElements() {
        if (properties == null) initConfig();
        return Boolean.parseBoolean(properties.getProperty("highlightElements", "false"));
    }
}
