package com.qkart.driver;

import com.qkart.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * Manager class for browser-specific options.
 * Configures browser options based on framework configuration.
 */
public final class OptionsManager {
    private static final Logger log = LogManager.getLogger(OptionsManager.class);

    private OptionsManager() {
        // Private constructor to prevent instantiation
    }

    /**
     * Creates Chrome browser options.
     *
     * @return Configured ChromeOptions
     */
    public static ChromeOptions getChromeOptions() {
        log.debug("Configuring Chrome options");
        ChromeOptions options = new ChromeOptions();

        // Essential options
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-infobars");

        // Performance options
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        // Headless mode
        if (ConfigManager.isHeadless()) {
            log.info("Running Chrome in headless mode");
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        }

        return options;
    }

    /**
     * Creates Firefox browser options.
     *
     * @return Configured FirefoxOptions
     */
    public static FirefoxOptions getFirefoxOptions() {
        log.debug("Configuring Firefox options");
        FirefoxOptions options = new FirefoxOptions();

        // Headless mode
        if (ConfigManager.isHeadless()) {
            log.info("Running Firefox in headless mode");
            options.addArguments("-headless");
            options.addArguments("--width=1920");
            options.addArguments("--height=1080");
        }

        return options;
    }

    /**
     * Creates Edge browser options.
     *
     * @return Configured EdgeOptions
     */
    public static EdgeOptions getEdgeOptions() {
        log.debug("Configuring Edge options");
        EdgeOptions options = new EdgeOptions();

        // Essential options
        options.addArguments("--start-maximized");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-popup-blocking");

        // Headless mode
        if (ConfigManager.isHeadless()) {
            log.info("Running Edge in headless mode");
            options.addArguments("--headless");
            options.addArguments("--window-size=1920,1080");
        }

        return options;
    }
}