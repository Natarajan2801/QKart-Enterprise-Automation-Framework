package com.qkart.utils;

import com.qkart.constants.FrameworkConstants;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for capturing screenshots during test execution.
 * Supports both file-based and Base64-encoded screenshot capture.
 */
public final class ScreenshotUtils {
    private static final Logger log = LogManager.getLogger(ScreenshotUtils.class);
    private static final String SCREENSHOT_PATH = FrameworkConstants.REPORT_PATH + "screenshots/";

    private ScreenshotUtils() {
        // Private constructor to prevent instantiation
    }

    /**
     * Captures a screenshot and returns it as a Base64 encoded string.
     * Useful for embedding in HTML reports.
     *
     * @param driver The WebDriver instance
     * @return Base64 encoded screenshot string, or null if capture fails
     */
    public static String captureAsBase64(WebDriver driver) {
        try {
            if (driver == null) {
                log.warn("Cannot capture screenshot - WebDriver is null");
                return null;
            }
            String base64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
            log.debug("Screenshot captured as Base64 successfully");
            return base64;
        } catch (Exception e) {
            log.error("Failed to capture screenshot as Base64: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Captures a screenshot and saves it to a file.
     *
     * @param driver   The WebDriver instance
     * @param testName The name of the test (used for filename)
     * @return The absolute path to the saved screenshot, or null if capture fails
     */
    public static String captureToFile(WebDriver driver, String testName) {
        try {
            if (driver == null) {
                log.warn("Cannot capture screenshot - WebDriver is null");
                return null;
            }

            // Create screenshot directory if it doesn't exist
            File screenshotDir = new File(SCREENSHOT_PATH);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }

            // Generate unique filename
            String timestamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS").format(new Date());
            String fileName = testName.replaceAll("[^a-zA-Z0-9]", "_") + "_" + timestamp + ".png";
            String filePath = SCREENSHOT_PATH + fileName;

            // Capture and save screenshot
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destFile = new File(filePath);
            FileUtils.copyFile(srcFile, destFile);

            log.info("Screenshot saved: {}", filePath);
            return filePath;
        } catch (Exception e) {
            log.error("Failed to capture screenshot to file: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Captures a screenshot on test failure with automatic naming.
     *
     * @param driver   The WebDriver instance
     * @param testName The name of the failed test
     * @return The absolute path to the saved screenshot
     */
    public static String captureOnFailure(WebDriver driver, String testName) {
        return captureToFile(driver, "FAILED_" + testName);
    }
}

