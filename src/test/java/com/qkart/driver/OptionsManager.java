package com.qkart.driver;

import com.qkart.utils.ConfigLoader;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

public class OptionsManager {

    public static ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-popup-blocking");

        if (Boolean.parseBoolean(ConfigLoader.get("headless"))) {
            options.addArguments("--headless=new");
        }
        return options;
    }

    public static FirefoxOptions getFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        if (Boolean.parseBoolean(ConfigLoader.get("headless"))) {
            options.addArguments("-headless");
        }
        return options;
    }

    public static EdgeOptions getEdgeOptions() {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--start-maximized");
        if (Boolean.parseBoolean(ConfigLoader.get("headless"))) {
            options.addArguments("--headless");
        }
        return options;
    }
}