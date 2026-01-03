package com.qkart.driver;

import com.qkart.utils.ConfigLoader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DriverFactory {
    private static final Logger log = LogManager.getLogger(DriverFactory.class);

    public static WebDriver createDriver() {
        String browser = ConfigLoader.get("browser").toLowerCase();
        log.info("Initializing Driver for Browser: " + browser);

        switch (browser) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                return new FirefoxDriver(OptionsManager.getFirefoxOptions());
            case "edge":
                WebDriverManager.edgedriver().setup();
                return new EdgeDriver(OptionsManager.getEdgeOptions());
            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                return new ChromeDriver(OptionsManager.getChromeOptions());
        }
    }
}