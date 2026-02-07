package com.qkart.pages;

import com.qkart.config.ConfigManager;
import com.qkart.constants.FrameworkConstants;
import com.qkart.enums.WaitStrategy;
import com.qkart.exceptions.ElementNotFoundException;
import com.qkart.utils.WaitUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Base class for all Page Objects.
 * Provides common functionality for interacting with web elements.
 * Implements fluent pattern for method chaining.
 */
public class BasePage {
    protected static final Logger log = LogManager.getLogger(BasePage.class);
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigManager.getExplicitWait()));
    }

    /**
     * Performs explicit wait for an element based on the specified strategy.
     *
     * @param strategy The wait strategy to use
     * @param locator  The element locator
     * @return The found WebElement
     */
    protected WebElement performExplicitWait(WaitStrategy strategy, By locator) {
        log.debug("Waiting for element [{}] with strategy [{}]", locator, strategy);
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
            throw new ElementNotFoundException(locator, ConfigManager.getExplicitWait(), e);
        }
    }

    /**
     * Clicks on an element with retry mechanism for stale elements.
     * Returns 'this' for fluent chaining.
     */
    @SuppressWarnings("unchecked")
    protected <T extends BasePage> T click(By locator, WaitStrategy strategy) {
        log.info("Clicking on element: {}", locator);
        int attempts = 0;
        while (attempts < FrameworkConstants.STALE_ELEMENT_RETRY_COUNT) {
            try {
                WebElement element = performExplicitWait(strategy, locator);
                highlightElement(element);
                element.click();
                log.debug("Successfully clicked on element: {}", locator);
                return (T) this;
            } catch (StaleElementReferenceException e) {
                attempts++;
                log.warn("StaleElementReferenceException on click, attempt {}/{}",
                        attempts, FrameworkConstants.STALE_ELEMENT_RETRY_COUNT);
            }
        }
        throw new ElementNotFoundException("Failed to click element after retries: " + locator);
    }

    /**
     * Sends text to an input element with retry mechanism.
     * Returns 'this' for fluent chaining.
     */
    @SuppressWarnings("unchecked")
    protected <T extends BasePage> T sendKeys(By locator, String text, WaitStrategy strategy) {
        log.info("Sending keys to element: {} with text: {}", locator, text);
        int attempts = 0;
        while (attempts < FrameworkConstants.STALE_ELEMENT_RETRY_COUNT) {
            try {
                WebElement element = performExplicitWait(strategy, locator);
                highlightElement(element);
                element.clear();
                element.sendKeys(text);
                log.debug("Successfully sent keys to element: {}", locator);
                return (T) this;
            } catch (StaleElementReferenceException e) {
                attempts++;
                log.warn("StaleElementReferenceException on sendKeys, attempt {}/{}",
                        attempts, FrameworkConstants.STALE_ELEMENT_RETRY_COUNT);
            }
        }
        throw new ElementNotFoundException("Failed to send keys to element after retries: " + locator);
    }

    /**
     * Gets text from an element.
     */
    protected String getText(By locator, WaitStrategy strategy) {
        log.debug("Getting text from element: {}", locator);
        String text = performExplicitWait(strategy, locator).getText();
        log.debug("Retrieved text: {}", text);
        return text;
    }

    /**
     * Click with default CLICKABLE strategy.
     */
    protected <T extends BasePage> T click(By locator) {
        return click(locator, WaitStrategy.CLICKABLE);
    }

    /**
     * SendKeys with default VISIBLE strategy.
     */
    protected <T extends BasePage> T sendKeys(By locator, String text) {
        return sendKeys(locator, text, WaitStrategy.VISIBLE);
    }

    /**
     * Checks if an element is displayed.
     */
    protected boolean isDisplayed(By locator) {
        try {
            return performExplicitWait(WaitStrategy.VISIBLE, locator).isDisplayed();
        } catch (Exception e) {
            log.debug("Element not displayed: {}", locator);
            return false;
        }
    }

    /**
     * Checks if an element is displayed with custom timeout.
     */
    protected boolean isDisplayed(By locator, int timeoutInSeconds) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            return customWait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Waits for element to be invisible.
     */
    protected boolean waitForInvisibility(By locator) {
        log.debug("Waiting for element to be invisible: {}", locator);
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Waits for URL to contain specific text.
     */
    protected boolean waitForUrlContains(String partialUrl) {
        log.debug("Waiting for URL to contain: {}", partialUrl);
        return wait.until(ExpectedConditions.urlContains(partialUrl));
    }

    /**
     * Waits for URL to end with specific text.
     */
    protected boolean waitForUrlEndsWith(String urlSuffix) {
        log.debug("Waiting for URL to end with: {}", urlSuffix);
        return wait.until(driver -> driver.getCurrentUrl().endsWith(urlSuffix));
    }

    /**
     * Waits for element to be present.
     */
    protected WebElement waitForPresence(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Waits for all elements to be present.
     */
    protected List<WebElement> waitForAllPresence(By locator) {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    /**
     * Performs JavaScript click on an element.
     */
    @SuppressWarnings("unchecked")
    protected <T extends BasePage> T jsClick(By locator) {
        log.info("Performing JS click on element: {}", locator);
        WebElement element = driver.findElement(locator);
        highlightElement(element);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
        return (T) this;
    }

    /**
     * Scrolls element into view.
     */
    protected void scrollIntoView(By locator) {
        log.debug("Scrolling element into view: {}", locator);
        WebElement element = driver.findElement(locator);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }

    /**
     * Gets the current page title.
     */
    protected String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Gets the current page URL.
     */
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Highlights an element for debugging purposes (if enabled in config).
     */
    private void highlightElement(WebElement element) {
        try {
            if (ConfigManager.shouldHighlightElements()) {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                String originalStyle = element.getAttribute("style");
                js.executeScript("arguments[0].setAttribute('style', 'border: 2px solid red; background: yellow;');", element);
                // Reset style after a brief moment (non-blocking)
                js.executeScript(
                        "setTimeout(function(){ arguments[0].setAttribute('style', arguments[1]); }, 300);",
                        element, originalStyle != null ? originalStyle : ""
                );
            }
        } catch (Exception e) {
            // Ignore highlighting errors
        }
    }

    /**
     * Waits for page to fully load.
     */
    protected void waitForPageLoad() {
        WaitUtils.waitForPageLoad(driver, ConfigManager.getPageLoadTimeout());
    }
}

