package com.qkart.pages;

import com.qkart.constants.LocatorRepository;
import com.qkart.constants.StringConstants;
import com.qkart.utils.ConfigLoader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {
    String url = ConfigLoader.get("url") + StringConstants.LOGIN_ENDPOINT;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToLoginPage() {
        if (!driver.getCurrentUrl().equals(this.url)) {
            driver.get(this.url);
        }
    }

    public void performLogin(String username, String password) {
        sendKeys(LocatorRepository.Login.USERNAME_INPUT, username);
        sendKeys(LocatorRepository.Login.PASSWORD_INPUT, password);
        click(LocatorRepository.Login.LOGIN_BUTTON);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(LocatorRepository.Login.LOGIN_BUTTON));
    }

    public boolean verifyUserLoggedIn(String expectedUsername) {
        try {
            String actualText = getText(LocatorRepository.Login.USERNAME_LABEL, com.qkart.enums.WaitStrategy.VISIBLE);
            return actualText.equals(expectedUsername);
        } catch (Exception e) {
            return false;
        }
    }
}