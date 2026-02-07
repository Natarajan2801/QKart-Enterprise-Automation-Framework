package com.qkart.listeners;

import com.qkart.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Retry analyzer for handling flaky tests.
 * Uses thread-safe counters to track retries per test method.
 */
public class RetryAnalyzer implements IRetryAnalyzer {
    private static final Logger log = LogManager.getLogger(RetryAnalyzer.class);

    // Thread-safe map to track retry count per test method
    private static final ConcurrentHashMap<String, AtomicInteger> retryCountMap = new ConcurrentHashMap<>();

    @Override
    public boolean retry(ITestResult result) {
        if (!result.isSuccess()) {
            String testMethodName = getTestMethodIdentifier(result);
            int maxRetry = ConfigManager.getRetryCount();

            AtomicInteger counter = retryCountMap.computeIfAbsent(testMethodName, k -> new AtomicInteger(0));
            int currentCount = counter.incrementAndGet();

            if (currentCount <= maxRetry) {
                log.warn("Retrying test [{}], attempt {}/{}", testMethodName, currentCount, maxRetry);
                return true;
            }
            log.error("Test [{}] failed after {} retry attempts", testMethodName, maxRetry);
        }
        return false;
    }

    /**
     * Creates a unique identifier for each test method invocation.
     */
    private String getTestMethodIdentifier(ITestResult result) {
        return result.getTestClass().getName() + "." + result.getMethod().getMethodName() +
               "_" + Thread.currentThread().getId();
    }

    /**
     * Resets the retry count for a specific test.
     * Can be called from test listener to clean up after test completion.
     */
    public static void resetRetryCount(String testMethodName) {
        retryCountMap.remove(testMethodName);
    }

    /**
     * Clears all retry counts.
     */
    public static void resetAll() {
        retryCountMap.clear();
    }
}