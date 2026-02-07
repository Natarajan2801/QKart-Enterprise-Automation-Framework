package com.qkart.pages;

import com.qkart.config.ConfigManager;
import com.qkart.constants.LocatorRepository;
import com.qkart.constants.StringConstants;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.sql.Timestamp;

/**
 * Page Object for the Register Page.
 * Implements fluent pattern for method chaining.
 */
public class RegisterPage extends BasePage {
    private final String url;
    public String lastGeneratedUsername = "";

    public RegisterPage(WebDriver driver) {
        super(driver);
        this.url = ConfigManager.getUrl() + StringConstants.REGISTER_ENDPOINT;
    }

    /**
     * Navigates to the register page.
     * @return this RegisterPage instance for chaining
     */
    public RegisterPage navigateToRegisterPage() {
        log.info("Navigating to Register page: {}", url);
        if (!driver.getCurrentUrl().equals(url)) {
            driver.get(url);
            waitForPageLoad();
        }
        return this;
    }

    /**
     * Registers a new user.
     * @param username The base username
     * @param password The password
     * @param makeDynamic Whether to append timestamp to username
     * @return true if registration was successful
     */
    public boolean registerUser(String username, String password, boolean makeDynamic) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String finalUsername = makeDynamic ? username + "_" + timestamp.getTime() : username;

        log.info("Registering user: {}", finalUsername);

        sendKeys(LocatorRepository.Register.USERNAME_INPUT, finalUsername);
        sendKeys(LocatorRepository.Register.PASSWORD_INPUT, password);
        sendKeys(LocatorRepository.Register.CONFIRM_PASSWORD_INPUT, password);
        click(LocatorRepository.Register.REGISTER_BUTTON);

        this.lastGeneratedUsername = finalUsername;

        try {
            wait.until(ExpectedConditions.urlContains(StringConstants.LOGIN_ENDPOINT));
            log.info("Registration successful for user: {}", finalUsername);
            return true;
        } catch (Exception e) {
            log.warn("Registration failed for user: {}", finalUsername);
            return false;
        }
    }

    /**
     * Enters username in the registration form.
     * @param username The username to enter
     * @return this RegisterPage instance for chaining
     */
    public RegisterPage enterUsername(String username) {
        log.info("Entering username: {}", username);
        sendKeys(LocatorRepository.Register.USERNAME_INPUT, username);
        return this;
    }

    /**
     * Enters password in the registration form.
     * @param password The password to enter
     * @return this RegisterPage instance for chaining
     */
    public RegisterPage enterPassword(String password) {
        log.info("Entering password");
        sendKeys(LocatorRepository.Register.PASSWORD_INPUT, password);
        return this;
    }

    /**
     * Enters confirm password in the registration form.
     * @param password The password to confirm
     * @return this RegisterPage instance for chaining
     */
    public RegisterPage enterConfirmPassword(String password) {
        log.info("Entering confirm password");
        sendKeys(LocatorRepository.Register.CONFIRM_PASSWORD_INPUT, password);
        return this;
    }

    /**
     * Clicks the register button.
     * @return LoginPage instance if successful
     */
    public LoginPage clickRegisterButton() {
        log.info("Clicking register button");
        click(LocatorRepository.Register.REGISTER_BUTTON);
        wait.until(ExpectedConditions.urlContains(StringConstants.LOGIN_ENDPOINT));
        return new LoginPage(driver);
    }

    /**
     * Gets the last generated username.
     * @return The last generated username
     */
    public String getLastGeneratedUsername() {
        return lastGeneratedUsername;
    }
}