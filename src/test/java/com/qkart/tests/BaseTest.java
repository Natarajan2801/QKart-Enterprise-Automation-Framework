package com.qkart.tests;

import com.qkart.driver.DriverFactory;
import com.qkart.utils.ConfigLoader;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public class BaseTest {
    protected static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    @BeforeMethod
    public void setUp() {
        WebDriver updatedDriver = DriverFactory.createDriver();
        driver.set(updatedDriver);

        getDriver().manage().window().maximize();
        int waitTime = Integer.parseInt(ConfigLoader.get("implicitWait"));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(waitTime));
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    @AfterMethod
    public void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();
            driver.remove();
        }
    }
}