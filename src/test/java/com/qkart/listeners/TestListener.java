package com.qkart.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.qkart.reports.ExtentManager;
import com.qkart.tests.BaseTest;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    // 1. Initialize Report Manager
    private static ExtentReports extent = ExtentManager.initReports();

    // 2. ThreadLocal is MANDATORY for Parallel Execution
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        // Create test entry in report
        ExtentTest extentTest = extent.createTest(result.getMethod().getDescription());
        test.set(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        if (test.get() != null) {
            test.get().log(Status.PASS, "Test Passed");
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        // DEFENSIVE CODING:
        // If DataProvider fails, onTestStart is skipped. test.get() will be null.
        // We must create the test node here to prevent NullPointerException.
        if (test.get() == null) {
            ExtentTest extentTest = extent.createTest(result.getMethod().getDescription() != null
                    ? result.getMethod().getDescription()
                    : result.getName());
            test.set(extentTest);
        }

        // Log the error
        test.get().fail(result.getThrowable());

        // Capture Screenshot (Only if Driver is alive)
        try {
            if (BaseTest.getDriver() != null) {
                String base64Screenshot = ((TakesScreenshot) BaseTest.getDriver()).getScreenshotAs(OutputType.BASE64);
                test.get().addScreenCaptureFromBase64String(base64Screenshot, "Failure Screenshot");
            }
        } catch (Exception e) {
            test.get().log(Status.WARNING, "Unable to capture screenshot: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // DEFENSIVE CODING for Skipped tests
        if (test.get() == null) {
            ExtentTest extentTest = extent.createTest(result.getMethod().getDescription() != null
                    ? result.getMethod().getDescription()
                    : result.getName());
            test.set(extentTest);
        }
        test.get().log(Status.SKIP, "Test Skipped: " + result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        if (extent != null) {
            extent.flush();
        }
    }
}