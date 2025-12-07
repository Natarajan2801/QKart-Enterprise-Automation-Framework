package com.qkart.pages;

import com.qkart.constants.LocatorRepository;
import com.qkart.constants.StringConstants;
import com.qkart.utils.DynamicXpath;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutPage extends BasePage {

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public void addNewAddress(String address) {
        click(LocatorRepository.Checkout.ADD_NEW_ADDRESS_BTN);
        sendKeys(LocatorRepository.Checkout.ADDRESS_INPUT_BOX, address);
        click(LocatorRepository.Checkout.ADD_ADDRESS_SAVE_BTN);
        By addressText = DynamicXpath.get(LocatorRepository.Checkout.ADDRESS_TEXT_XPATH, address);
        wait.until(ExpectedConditions.visibilityOfElementLocated(addressText));
    }

    public void selectAddress(String addressToSelect) {
        By addressRadio = DynamicXpath.get(LocatorRepository.Checkout.ADDRESS_RADIO_BTN_XPATH, addressToSelect);
        jsClick(addressRadio);
    }

    public void placeOrder() {
        click(LocatorRepository.Checkout.PLACE_ORDER_BTN);
    }

    public boolean verifyInsufficientBalanceMessage() {
        try {
            WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(LocatorRepository.Checkout.SNACKBAR_MESSAGE));
            return alert.getText().equals(StringConstants.INSUFFICIENT_BALANCE_ERR);
        } catch (Exception e) {
            return false;
        }
    }
}