package com.qkart.pages;

import com.qkart.constants.FrameworkConstants;
import com.qkart.enums.WaitStrategy;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(FrameworkConstants.EXPLICIT_WAIT));
    }

    protected WebElement performExplicitWait(WaitStrategy strategy, By locator) {
        WebElement element = null;
        try {
            switch (strategy) {
                case CLICKABLE:
                    element = wait.until(ExpectedConditions.elementToBeClickable(locator));
                    break;
                case VISIBLE:
                    element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                    break;
                case PRESENCE:
                    element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
                    break;
                case NONE:
                    element = driver.findElement(locator);
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException("Element Not Found or Timed Out: " + locator.toString());
        }
        return element;
    }

    protected void click(By locator, WaitStrategy strategy) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                WebElement element = performExplicitWait(strategy, locator);
                element.click();
                break;
            } catch (StaleElementReferenceException e) {
                attempts++;
            } catch (Exception e) {
                throw e;
            }
        }
    }

    protected void sendKeys(By locator, String text, WaitStrategy strategy) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                WebElement element = performExplicitWait(strategy, locator);
                element.clear();
                element.sendKeys(text);
                break;
            } catch (StaleElementReferenceException e) {
                attempts++;
            }
        }
    }

    protected String getText(By locator, WaitStrategy strategy) {
        return performExplicitWait(strategy, locator).getText();
    }

    protected void click(By locator) {
        click(locator, WaitStrategy.CLICKABLE);
    }

    protected void sendKeys(By locator, String text) {
        sendKeys(locator, text, WaitStrategy.VISIBLE);
    }

    protected boolean isDisplayed(By locator) {
        try {
            return performExplicitWait(WaitStrategy.VISIBLE, locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected void waitForSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void jsClick(By locator) {
        WebElement element = driver.findElement(locator);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
    }
}