package com.qkart.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.qkart.config.ConfigManager;
import com.qkart.reports.ExtentManager;
import com.qkart.tests.BaseTest;
import com.qkart.utils.ScreenshotUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG listener for test lifecycle events.
 * Handles ExtentReports reporting integration.
 */
public class TestListener implements ITestListener {
    private static final Logger log = LogManager.getLogger(TestListener.class);

    // Initialize Report Manager
    private static ExtentReports extent = ExtentManager.initReports();

    // ThreadLocal is MANDATORY for Parallel Execution
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {
        log.info("========== TEST SUITE STARTED: {} ==========", context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getDescription() != null
                ? result.getMethod().getDescription()
                : result.getMethod().getMethodName();

        log.info(">>> Starting Test: {}", testName);

        // Create test entry in Extent Report
        ExtentTest extentTest = extent.createTest(testName);
        test.set(extentTest);

        // Add test parameters if available
        if (result.getParameters().length > 0) {
            StringBuilder params = new StringBuilder("Parameters: ");
            for (Object param : result.getParameters()) {
                params.append(param).append(", ");
            }
            test.get().info(params.toString());
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = getTestName(result);
        log.info("<<< Test PASSED: {}", testName);

        if (test.get() != null) {
            test.get().log(Status.PASS, "Test Passed Successfully");
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = getTestName(result);
        log.error("<<< Test FAILED: {}", testName);
        log.error("Failure Reason: {}", result.getThrowable().getMessage());

        // DEFENSIVE CODING: Create test node if not exists
        if (test.get() == null) {
            ExtentTest extentTest = extent.createTest(testName);
            test.set(extentTest);
        }

        // Log the error
        test.get().fail(result.getThrowable());

        // Capture Screenshot on failure
        if (ConfigManager.shouldTakeScreenshotOnFailure()) {
            captureScreenshot(testName);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = getTestName(result);
        log.warn("<<< Test SKIPPED: {}", testName);

        // DEFENSIVE CODING for Skipped tests
        if (test.get() == null) {
            ExtentTest extentTest = extent.createTest(testName);
            test.set(extentTest);
        }

        test.get().log(Status.SKIP, "Test Skipped");
        if (result.getThrowable() != null) {
            test.get().log(Status.SKIP, result.getThrowable());
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("========== TEST SUITE FINISHED: {} ==========", context.getName());
        log.info("Passed: {} | Failed: {} | Skipped: {}",
                context.getPassedTests().size(),
                context.getFailedTests().size(),
                context.getSkippedTests().size());

        if (extent != null) {
            extent.flush();
            log.info("Extent Report generated successfully");
        }

        // Clear retry counter at the end of suite
        RetryAnalyzer.resetAll();
    }

    /**
     * Captures screenshot and attaches to Extent report.
     */
    private void captureScreenshot(String testName) {
        try {
            if (BaseTest.getDriver() != null) {
                String base64Screenshot = ScreenshotUtils.captureAsBase64(BaseTest.getDriver());

                if (base64Screenshot != null) {
                    // Attach to Extent Report
                    test.get().addScreenCaptureFromBase64String(base64Screenshot, "Failure Screenshot");
                    log.info("Screenshot captured and attached to report");
                }
            }
        } catch (Exception e) {
            log.error("Unable to capture screenshot: {}", e.getMessage());
            test.get().log(Status.WARNING, "Unable to capture screenshot: " + e.getMessage());
        }
    }

    /**
     * Gets the test name from the result.
     */
    private String getTestName(ITestResult result) {
        return result.getMethod().getDescription() != null
                ? result.getMethod().getDescription()
                : result.getMethod().getMethodName();
    }
}