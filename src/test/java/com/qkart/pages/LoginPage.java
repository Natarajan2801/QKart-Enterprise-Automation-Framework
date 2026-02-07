package com.qkart.pages;

import com.qkart.config.ConfigManager;
import com.qkart.constants.LocatorRepository;
import com.qkart.constants.StringConstants;
import com.qkart.enums.WaitStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object for the Login Page.
 * Implements fluent pattern for method chaining.
 */
public class LoginPage extends BasePage {
    private final String url;

    public LoginPage(WebDriver driver) {
        super(driver);
        this.url = ConfigManager.getUrl() + StringConstants.LOGIN_ENDPOINT;
    }

    /**
     * Navigates to the login page.
     * @return this LoginPage instance for chaining
     */
    public LoginPage navigateToLoginPage() {
        log.info("Navigating to Login page: {}", url);
        if (!driver.getCurrentUrl().equals(url)) {
            driver.get(url);
            waitForPageLoad();
        }
        return this;
    }

    /**
     * Performs login with the given credentials.
     * @param username The username to login with
     * @param password The password to login with
     * @return HomePage instance for chaining after successful login
     */
    public HomePage performLogin(String username, String password) {
        log.info("Performing login for user: {}", username);
        sendKeys(LocatorRepository.Login.USERNAME_INPUT, username);
        sendKeys(LocatorRepository.Login.PASSWORD_INPUT, password);
        click(LocatorRepository.Login.LOGIN_BUTTON);
        waitForInvisibility(LocatorRepository.Login.LOGIN_BUTTON);
        return new HomePage(driver);
    }

    /**
     * Enters username in the login form.
     * @param username The username to enter
     * @return this LoginPage instance for chaining
     */
    public LoginPage enterUsername(String username) {
        log.info("Entering username: {}", username);
        sendKeys(LocatorRepository.Login.USERNAME_INPUT, username);
        return this;
    }

    /**
     * Enters password in the login form.
     * @param password The password to enter
     * @return this LoginPage instance for chaining
     */
    public LoginPage enterPassword(String password) {
        log.info("Entering password");
        sendKeys(LocatorRepository.Login.PASSWORD_INPUT, password);
        return this;
    }

    /**
     * Clicks the login button.
     * @return HomePage instance for chaining
     */
    public HomePage clickLoginButton() {
        log.info("Clicking login button");
        click(LocatorRepository.Login.LOGIN_BUTTON);
        waitForInvisibility(LocatorRepository.Login.LOGIN_BUTTON);
        return new HomePage(driver);
    }

    /**
     * Verifies if the user is logged in.
     * @param expectedUsername The expected username to verify
     * @return true if logged in with expected username
     */
    public boolean verifyUserLoggedIn(String expectedUsername) {
        log.info("Verifying user logged in: {}", expectedUsername);
        try {
            String actualText = getText(LocatorRepository.Login.USERNAME_LABEL, WaitStrategy.VISIBLE);
            boolean isLoggedIn = actualText.equals(expectedUsername);
            log.info("Login verification result: {}", isLoggedIn);
            return isLoggedIn;
        } catch (Exception e) {
            log.warn("Login verification failed: {}", e.getMessage());
            return false;
        }
    }
}