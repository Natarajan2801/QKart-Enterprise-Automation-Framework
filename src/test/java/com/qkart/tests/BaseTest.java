package com.qkart.tests;

import com.qkart.config.ConfigManager;
import com.qkart.driver.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

/**
 * Base test class that all test classes should extend.
 * Handles WebDriver lifecycle management with thread safety.
 */
public class BaseTest {
    private static final Logger log = LogManager.getLogger(BaseTest.class);
    protected static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    /**
     * Sets up the WebDriver before each test method.
     */
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        log.info("========== TEST SETUP STARTED ==========");
        WebDriver webDriver = DriverFactory.createDriver();
        driver.set(webDriver);

        // Configure timeouts
        getDriver().manage().window().maximize();
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigManager.getImplicitWait()));
        getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigManager.getPageLoadTimeout()));

        log.info("WebDriver initialized and configured successfully");
        log.info("Browser: {} | Headless: {}",
                ConfigManager.getBrowser().getBrowserName(),
                ConfigManager.isHeadless());
    }

    /**
     * Gets the WebDriver instance for the current thread.
     *
     * @return The WebDriver instance
     */
    public static WebDriver getDriver() {
        return driver.get();
    }

    /**
     * Tears down the WebDriver after each test method.
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        log.info("========== TEST TEARDOWN STARTED ==========");
        if (getDriver() != null) {
            try {
                getDriver().quit();
                log.info("WebDriver quit successfully");
            } catch (Exception e) {
                log.error("Error while quitting WebDriver: {}", e.getMessage());
            } finally {
                driver.remove();
            }
        }
    }
}