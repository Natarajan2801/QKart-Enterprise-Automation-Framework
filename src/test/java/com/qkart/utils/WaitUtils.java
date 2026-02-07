package com.qkart.utils;

import com.qkart.config.ConfigManager;
import com.qkart.enums.WaitStrategy;
import com.qkart.exceptions.ElementNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

/**
 * Utility class providing advanced wait mechanisms for Selenium WebDriver.
 * Eliminates the need for Thread.sleep() throughout the framework.
 */
public final class WaitUtils {
    private static final Logger log = LogManager.getLogger(WaitUtils.class);

    private WaitUtils() {
        // Private constructor to prevent instantiation
    }

    /**
     * Creates a WebDriverWait instance with default timeout.
     */
    public static WebDriverWait getDefaultWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(ConfigManager.getExplicitWait()));
    }

    /**
     * Creates a WebDriverWait instance with custom timeout.
     */
    public static WebDriverWait getWait(WebDriver driver, int timeoutInSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
    }

    /**
     * Creates a FluentWait instance for more granular control.
     */
    public static FluentWait<WebDriver> getFluentWait(WebDriver driver, int timeoutInSeconds, int pollingIntervalMs) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutInSeconds))
                .pollingEvery(Duration.ofMillis(pollingIntervalMs))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
    }

    /**
     * Waits for an element based on the specified strategy.
     */
    public static WebElement waitForElement(WebDriver driver, By locator, WaitStrategy strategy) {
        return waitForElement(driver, locator, strategy, ConfigManager.getExplicitWait());
    }

    /**
     * Waits for an element based on the specified strategy with custom timeout.
     */
    public static WebElement waitForElement(WebDriver driver, By locator, WaitStrategy strategy, int timeoutInSeconds) {
        log.debug("Waiting for element [{}] with strategy [{}]", locator, strategy);
        WebDriverWait wait = getWait(driver, timeoutInSeconds);

        try {
            switch (strategy) {
                case CLICKABLE:
                    return wait.until(ExpectedConditions.elementToBeClickable(locator));
                case VISIBLE:
                    return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                case PRESENCE:
                    return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
                case NONE:
                    return driver.findElement(locator);
                default:
                    return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            }
        } catch (TimeoutException e) {
            throw new ElementNotFoundException(locator, timeoutInSeconds, e);
        }
    }

    /**
     * Waits for all elements matching the locator to be present.
     */
    public static List<WebElement> waitForElements(WebDriver driver, By locator, int timeoutInSeconds) {
        log.debug("Waiting for elements [{}]", locator);
        return getWait(driver, timeoutInSeconds)
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    /**
     * Waits for an element to become invisible.
     */
    public static boolean waitForInvisibility(WebDriver driver, By locator, int timeoutInSeconds) {
        log.debug("Waiting for element [{}] to become invisible", locator);
        return getWait(driver, timeoutInSeconds)
                .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Waits for URL to contain specific text.
     */
    public static boolean waitForUrlContains(WebDriver driver, String partialUrl, int timeoutInSeconds) {
        log.debug("Waiting for URL to contain [{}]", partialUrl);
        return getWait(driver, timeoutInSeconds)
                .until(ExpectedConditions.urlContains(partialUrl));
    }

    /**
     * Waits for URL to match exactly.
     */
    public static boolean waitForUrlToBe(WebDriver driver, String url, int timeoutInSeconds) {
        log.debug("Waiting for URL to be [{}]", url);
        return getWait(driver, timeoutInSeconds)
                .until(ExpectedConditions.urlToBe(url));
    }

    /**
     * Waits for page to finish loading using JavaScript.
     */
    public static void waitForPageLoad(WebDriver driver, int timeoutInSeconds) {
        log.debug("Waiting for page to finish loading");
        getWait(driver, timeoutInSeconds).until(
                webDriver -> ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState").equals("complete")
        );
    }

    /**
     * Waits for AJAX calls to complete (for jQuery-based applications).
     */
    public static void waitForAjaxComplete(WebDriver driver, int timeoutInSeconds) {
        log.debug("Waiting for AJAX calls to complete");
        getWait(driver, timeoutInSeconds).until(
                webDriver -> {
                    JavascriptExecutor js = (JavascriptExecutor) webDriver;
                    return (Boolean) js.executeScript(
                            "return (typeof jQuery !== 'undefined') ? jQuery.active == 0 : true"
                    );
                }
        );
    }

    /**
     * Waits for element text to be present.
     */
    public static boolean waitForTextPresent(WebDriver driver, By locator, String text, int timeoutInSeconds) {
        log.debug("Waiting for text [{}] in element [{}]", text, locator);
        return getWait(driver, timeoutInSeconds)
                .until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    /**
     * Waits for element attribute to have a specific value.
     */
    public static boolean waitForAttributeValue(WebDriver driver, By locator, String attribute,
                                                 String value, int timeoutInSeconds) {
        log.debug("Waiting for attribute [{}] to be [{}] on element [{}]", attribute, value, locator);
        return getWait(driver, timeoutInSeconds)
                .until(ExpectedConditions.attributeToBe(locator, attribute, value));
    }

    /**
     * Waits for a specific number of browser windows/tabs.
     */
    public static boolean waitForWindowCount(WebDriver driver, int expectedCount, int timeoutInSeconds) {
        log.debug("Waiting for window count to be [{}]", expectedCount);
        return getWait(driver, timeoutInSeconds)
                .until(ExpectedConditions.numberOfWindowsToBe(expectedCount));
    }

    /**
     * Custom wait with a predicate function.
     */
    public static <T> T waitForCondition(WebDriver driver, Function<WebDriver, T> condition, int timeoutInSeconds) {
        return getWait(driver, timeoutInSeconds).until(condition);
    }
}

