package com.qkart.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.qkart.constants.FrameworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExtentManager {
    private static final Logger log = LogManager.getLogger(ExtentManager.class);
    private static ExtentReports extent;

    private ExtentManager() {
    }

    public static ExtentReports initReports() {
        if (extent == null) {
            File reportDir = new File(FrameworkConstants.REPORT_PATH);
            if (!reportDir.exists()) {
                reportDir.mkdirs();
            }

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss"));
            String reportPath = FrameworkConstants.REPORT_PATH + "QKart_Execution_Report_" + timestamp + ".html";

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setTheme(Theme.DARK);
            sparkReporter.config().setDocumentTitle("QKart Automation Report");
            sparkReporter.config().setReportName("QKart Test Execution Report");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("Application", "QKart E-Commerce");
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java", System.getProperty("java.version"));

            log.info("ExtentReports initialized at: {}", reportPath);
        }
        return extent;
    }

    public static void flushReports() {
        if (extent != null) {
            extent.flush();
        }
    }
}

