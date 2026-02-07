package com.qkart.driver;

import com.qkart.config.ConfigManager;
import com.qkart.enums.BrowserType;
import com.qkart.exceptions.BrowserInitializationException;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Factory class for creating WebDriver instances.
 * Supports Chrome, Firefox, and Edge browsers.
 */
public final class DriverFactory {
    private static final Logger log = LogManager.getLogger(DriverFactory.class);

    private DriverFactory() {
        // Private constructor to prevent instantiation
    }

    /**
     * Creates a WebDriver instance based on the configured browser type.
     *
     * @return A new WebDriver instance
     * @throws BrowserInitializationException if browser initialization fails
     */
    public static WebDriver createDriver() {
        BrowserType browserType = ConfigManager.getBrowser();
        log.info("Initializing WebDriver for browser: {}", browserType.getBrowserName());

        try {
            WebDriver driver;
            switch (browserType) {
                case FIREFOX:
                    WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver(OptionsManager.getFirefoxOptions());
                    break;
                case EDGE:
                    WebDriverManager.edgedriver().setup();
                    driver = new EdgeDriver(OptionsManager.getEdgeOptions());
                    break;
                case CHROME:
                default:
                    WebDriverManager.chromedriver().setup();
                    driver = new ChromeDriver(OptionsManager.getChromeOptions());
                    break;
            }
            log.info("WebDriver initialized successfully for browser: {}", browserType.getBrowserName());
            return driver;
        } catch (Exception e) {
            log.error("Failed to initialize WebDriver for browser: {}", browserType.getBrowserName(), e);
            throw new BrowserInitializationException(browserType.getBrowserName(), e);
        }
    }

    /**
     * Creates a WebDriver instance for a specific browser type.
     *
     * @param browserType The type of browser to create
     * @return A new WebDriver instance
     */
    public static WebDriver createDriver(BrowserType browserType) {
        log.info("Initializing WebDriver for specified browser: {}", browserType.getBrowserName());

        try {
            WebDriver driver;
            switch (browserType) {
                case FIREFOX:
                    WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver(OptionsManager.getFirefoxOptions());
                    break;
                case EDGE:
                    WebDriverManager.edgedriver().setup();
                    driver = new EdgeDriver(OptionsManager.getEdgeOptions());
                    break;
                case CHROME:
                default:
                    WebDriverManager.chromedriver().setup();
                    driver = new ChromeDriver(OptionsManager.getChromeOptions());
                    break;
            }
            log.info("WebDriver initialized successfully");
            return driver;
        } catch (Exception e) {
            log.error("Failed to initialize WebDriver", e);
            throw new BrowserInitializationException(browserType.getBrowserName(), e);
        }
    }
}