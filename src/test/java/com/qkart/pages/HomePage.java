package com.qkart.pages;

import com.qkart.constants.LocatorRepository;
import com.qkart.constants.StringConstants;
import com.qkart.utils.ConfigLoader;
import com.qkart.utils.DynamicXpath;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;
import java.util.stream.Collectors;

public class HomePage extends BasePage {
    String url = ConfigLoader.get("url");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void navigateToHome() {
        if (!driver.getCurrentUrl().equals(this.url)) {
            driver.get(this.url);
        }
    }

    public void performLogout() {
        if (isDisplayed(LocatorRepository.Home.LOGOUT_BUTTON)) {
            click(LocatorRepository.Home.LOGOUT_BUTTON);
            wait.until(ExpectedConditions.invisibilityOfElementLocated(LocatorRepository.Home.LOGOUT_BUTTON));
        }
    }

    public void searchForProduct(String product) {
        sendKeys(LocatorRepository.Home.SEARCH_BOX, product);
        wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(LocatorRepository.Home.SEARCH_RESULTS),
                ExpectedConditions.presenceOfElementLocated(LocatorRepository.Home.NO_RESULTS_MSG)
        ));
        waitForSeconds(1);
    }

    public List<WebElement> getSearchResultElements() {
        return driver.findElements(LocatorRepository.Home.SEARCH_RESULTS);
    }

    public boolean isNoResultFound() {
        return isDisplayed(LocatorRepository.Home.NO_RESULTS_MSG);
    }

    public void addProductToCart(String productName) {
        By addBtn = DynamicXpath.get(LocatorRepository.Home.PRODUCT_ADD_BUTTON_XPATH, productName);
        click(addBtn);
        By cartItem = DynamicXpath.get(LocatorRepository.Home.CART_ITEM_PRESENCE_XPATH, productName);
        wait.until(ExpectedConditions.presenceOfElementLocated(cartItem));
    }

    public void changeProductQuantityInCart(String productName, int newQuantity) {
        List<WebElement> cartItemsList = driver.findElements(LocatorRepository.Home.CART_ITEM_CONTAINER);

        for (WebElement item : cartItemsList) {
            if (item.findElement(LocatorRepository.Home.CART_ITEM_TITLE).getText().equals(productName)) {
                int currentQty = Integer.parseInt(item.findElement(LocatorRepository.Home.CART_ITEM_QTY).getText());

                while (currentQty != newQuantity) {
                    if (currentQty < newQuantity) {
                        item.findElements(By.tagName("button")).get(1).click();
                    } else {
                        item.findElements(By.tagName("button")).get(0).click();
                    }
                    waitForSeconds(1);
                    if(newQuantity == 0 && currentQty == 1) break;
                    currentQty = Integer.parseInt(item.findElement(LocatorRepository.Home.CART_ITEM_QTY).getText());
                }
                return;
            }
        }
    }

    public void clickCheckout() {
        click(LocatorRepository.Home.CHECKOUT_BUTTON);
        wait.until(ExpectedConditions.urlContains(StringConstants.CHECKOUT_ENDPOINT));
    }

    public List<String> getCartContents() {
        return driver.findElements(LocatorRepository.Home.CART_PRODUCT_TEXT_LIST)
                .stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public void navigateToContactUs() {
        click(LocatorRepository.Home.CONTACT_US_LINK);
        wait.until(ExpectedConditions.visibilityOfElementLocated(LocatorRepository.Home.CONTACT_US_MODAL));
    }

    public void clickPrivacyPolicy() {
        click(LocatorRepository.Home.PRIVACY_POLICY_LINK);
    }

    public void clickTermsOfService() {
        click(LocatorRepository.Home.TERMS_OF_SERVICE_LINK);
    }
}