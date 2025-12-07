package com.qkart.pages;

import com.qkart.constants.LocatorRepository;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ContactUsPage extends BasePage {

    public ContactUsPage(WebDriver driver) {
        super(driver);
    }

    public void fillContactUsForm(String name, String email, String message) {
        sendKeys(LocatorRepository.ContactUs.NAME_INPUT, name);
        sendKeys(LocatorRepository.ContactUs.EMAIL_INPUT, email);
        sendKeys(LocatorRepository.ContactUs.MESSAGE_INPUT, message);
        click(LocatorRepository.ContactUs.CONTACT_NOW_BTN);
    }

    public boolean isContactModalClosed() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(LocatorRepository.ContactUs.CONTACT_NOW_BTN));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}