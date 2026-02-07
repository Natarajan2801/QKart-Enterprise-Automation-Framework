package com.qkart.pages;

import com.qkart.constants.LocatorRepository;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object for the Contact Us modal/form.
 * Implements fluent pattern for method chaining.
 */
public class ContactUsPage extends BasePage {

    public ContactUsPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Fills the contact us form.
     * @param name The name to enter
     * @param email The email to enter
     * @param message The message to enter
     * @return this ContactUsPage instance for chaining
     */
    public ContactUsPage fillContactUsForm(String name, String email, String message) {
        log.info("Filling contact form - Name: {}, Email: {}", name, email);
        enterName(name);
        enterEmail(email);
        enterMessage(message);
        clickContactNowButton();
        return this;
    }

    /**
     * Enters name in the contact form.
     * @param name The name to enter
     * @return this ContactUsPage instance for chaining
     */
    public ContactUsPage enterName(String name) {
        log.debug("Entering name: {}", name);
        sendKeys(LocatorRepository.ContactUs.NAME_INPUT, name);
        return this;
    }

    /**
     * Enters email in the contact form.
     * @param email The email to enter
     * @return this ContactUsPage instance for chaining
     */
    public ContactUsPage enterEmail(String email) {
        log.debug("Entering email: {}", email);
        sendKeys(LocatorRepository.ContactUs.EMAIL_INPUT, email);
        return this;
    }

    /**
     * Enters message in the contact form.
     * @param message The message to enter
     * @return this ContactUsPage instance for chaining
     */
    public ContactUsPage enterMessage(String message) {
        log.debug("Entering message");
        sendKeys(LocatorRepository.ContactUs.MESSAGE_INPUT, message);
        return this;
    }

    /**
     * Clicks the Contact Now button.
     * @return this ContactUsPage instance for chaining
     */
    public ContactUsPage clickContactNowButton() {
        log.info("Clicking Contact Now button");
        click(LocatorRepository.ContactUs.CONTACT_NOW_BTN);
        return this;
    }

    /**
     * Checks if the contact modal has closed.
     * @return true if modal is closed
     */
    public boolean isContactModalClosed() {
        log.info("Checking if contact modal is closed");
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(LocatorRepository.ContactUs.CONTACT_NOW_BTN));
            log.info("Contact modal closed successfully");
            return true;
        } catch (Exception e) {
            log.warn("Contact modal still visible");
            return false;
        }
    }
}