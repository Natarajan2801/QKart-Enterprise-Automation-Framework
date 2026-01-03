package com.qkart.pages;

import com.qkart.constants.LocatorRepository;
import com.qkart.constants.StringConstants;
import com.qkart.utils.ConfigLoader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.sql.Timestamp;

public class RegisterPage extends BasePage {
    String url = ConfigLoader.get("url") + StringConstants.REGISTER_ENDPOINT;
    public String lastGeneratedUsername = "";

    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToRegisterPage() {
        if (!driver.getCurrentUrl().equals(this.url)) {
            driver.get(this.url);
        }
    }

    public boolean registerUser(String username, String password, boolean makeDynamic) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String finalUsername = makeDynamic ? username + "_" + timestamp.getTime() : username;

        sendKeys(LocatorRepository.Register.USERNAME_INPUT, finalUsername);
        sendKeys(LocatorRepository.Register.PASSWORD_INPUT, password);
        sendKeys(LocatorRepository.Register.CONFIRM_PASSWORD_INPUT, password);
        click(LocatorRepository.Register.REGISTER_BUTTON);

        this.lastGeneratedUsername = finalUsername;

        try {
            wait.until(ExpectedConditions.urlContains(StringConstants.LOGIN_ENDPOINT));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}