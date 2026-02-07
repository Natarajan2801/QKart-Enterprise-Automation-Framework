package com.qkart.constants;

/**
 * Central repository for all framework-wide constants.
 * Only contains static file paths and fixed constants.
 * For configurable values, use ConfigManager directly.
 */
public final class FrameworkConstants {

    private FrameworkConstants() {
        // Private constructor to prevent instantiation
    }

    // File Paths
    private static final String RESOURCE_PATH = System.getProperty("user.dir") + "/src/test/resources";
    public static final String CONFIG_FILE_PATH = RESOURCE_PATH + "/config.properties";
    public static final String EXCEL_DATA_FILE_PATH = RESOURCE_PATH + "/Dataset.xlsx";
    public static final String REPORT_PATH = System.getProperty("user.dir") + "/reports/";
    public static final String SCREENSHOT_PATH = REPORT_PATH + "screenshots/";
    public static final String LOG_PATH = System.getProperty("user.dir") + "/logs/";

    // Fixed Constants
    public static final int POLLING_INTERVAL_MS = 500;
    public static final int STALE_ELEMENT_RETRY_COUNT = 3;
}