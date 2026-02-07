package com.qkart.pages;

import com.qkart.constants.LocatorRepository;
import com.qkart.constants.StringConstants;
import com.qkart.utils.DynamicXpath;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object for the Checkout Page.
 * Implements fluent pattern for method chaining.
 */
public class CheckoutPage extends BasePage {

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Adds a new address.
     * @param address The address to add
     * @return this CheckoutPage instance for chaining
     */
    public CheckoutPage addNewAddress(String address) {
        log.info("Adding new address: {}", address);
        click(LocatorRepository.Checkout.ADD_NEW_ADDRESS_BTN);
        sendKeys(LocatorRepository.Checkout.ADDRESS_INPUT_BOX, address);
        click(LocatorRepository.Checkout.ADD_ADDRESS_SAVE_BTN);
        By addressText = DynamicXpath.get(LocatorRepository.Checkout.ADDRESS_TEXT_XPATH, address);
        wait.until(ExpectedConditions.visibilityOfElementLocated(addressText));
        return this;
    }

    /**
     * Selects an address.
     * @param addressToSelect The address to select
     * @return this CheckoutPage instance for chaining
     */
    public CheckoutPage selectAddress(String addressToSelect) {
        log.info("Selecting address: {}", addressToSelect);
        By addressRadio = DynamicXpath.get(LocatorRepository.Checkout.ADDRESS_RADIO_BTN_XPATH, addressToSelect);
        jsClick(addressRadio);
        return this;
    }

    /**
     * Places the order.
     * @return this CheckoutPage instance for chaining
     */
    public CheckoutPage placeOrder() {
        log.info("Placing order");
        click(LocatorRepository.Checkout.PLACE_ORDER_BTN);
        return this;
    }

    /**
     * Verifies if insufficient balance message is displayed.
     * @return true if message is displayed
     */
    public boolean verifyInsufficientBalanceMessage() {
        log.info("Verifying insufficient balance message");
        try {
            WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    LocatorRepository.Checkout.SNACKBAR_MESSAGE));
            boolean result = alert.getText().equals(StringConstants.INSUFFICIENT_BALANCE_ERR);
            log.info("Insufficient balance message displayed: {}", result);
            return result;
        } catch (Exception e) {
            log.warn("Insufficient balance message not found");
            return false;
        }
    }

    /**
     * Verifies if order was placed successfully by checking URL.
     * @return true if on thanks page
     */
    public boolean verifyOrderPlacedSuccessfully() {
        log.info("Verifying order placed successfully");
        try {
            return waitForUrlEndsWith(StringConstants.THANKS_ENDPOINT);
        } catch (Exception e) {
            log.warn("Order placement verification failed");
            return false;
        }
    }
}