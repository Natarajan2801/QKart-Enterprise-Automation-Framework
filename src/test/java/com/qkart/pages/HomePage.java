package com.qkart.pages;

import com.qkart.config.ConfigManager;
import com.qkart.constants.LocatorRepository;
import com.qkart.constants.StringConstants;
import com.qkart.utils.DynamicXpath;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object for the Home Page.
 * Implements fluent pattern for method chaining.
 */
public class HomePage extends BasePage {
    private final String url;

    public HomePage(WebDriver driver) {
        super(driver);
        this.url = ConfigManager.getUrl();
    }

    /**
     * Navigates to the home page.
     * @return this HomePage instance for chaining
     */
    public HomePage navigateToHome() {
        log.info("Navigating to Home page: {}", url);
        if (!driver.getCurrentUrl().equals(url)) {
            driver.get(url);
            waitForPageLoad();
        }
        return this;
    }

    /**
     * Performs logout if logged in.
     * @return this HomePage instance for chaining
     */
    public HomePage performLogout() {
        log.info("Performing logout");
        if (isDisplayed(LocatorRepository.Home.LOGOUT_BUTTON, 3)) {
            click(LocatorRepository.Home.LOGOUT_BUTTON);
            waitForInvisibility(LocatorRepository.Home.LOGOUT_BUTTON);
        }
        return this;
    }

    /**
     * Searches for a product.
     * @param product The product name to search
     * @return this HomePage instance for chaining
     */
    public HomePage searchForProduct(String product) {
        log.info("Searching for product: {}", product);
        sendKeys(LocatorRepository.Home.SEARCH_BOX, product);
        wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(LocatorRepository.Home.SEARCH_RESULTS),
                ExpectedConditions.presenceOfElementLocated(LocatorRepository.Home.NO_RESULTS_MSG)
        ));
        return this;
    }

    /**
     * Gets the list of search result elements.
     * @return List of WebElements representing search results
     */
    public List<WebElement> getSearchResultElements() {
        return driver.findElements(LocatorRepository.Home.SEARCH_RESULTS);
    }

    /**
     * Checks if no results found message is displayed.
     * @return true if no results found
     */
    public boolean isNoResultFound() {
        return isDisplayed(LocatorRepository.Home.NO_RESULTS_MSG);
    }

    /**
     * Adds a product to the cart.
     * @param productName The name of the product to add
     * @return this HomePage instance for chaining
     */
    public HomePage addProductToCart(String productName) {
        log.info("Adding product to cart: {}", productName);
        By addBtn = DynamicXpath.get(LocatorRepository.Home.PRODUCT_ADD_BUTTON_XPATH, productName);
        click(addBtn);
        By cartItem = DynamicXpath.get(LocatorRepository.Home.CART_ITEM_PRESENCE_XPATH, productName);
        wait.until(ExpectedConditions.presenceOfElementLocated(cartItem));
        return this;
    }

    /**
     * Changes the quantity of a product in the cart.
     * @param productName The name of the product
     * @param newQuantity The desired quantity
     * @return this HomePage instance for chaining
     */
    public HomePage changeProductQuantityInCart(String productName, int newQuantity) {
        log.info("Changing quantity of {} to {}", productName, newQuantity);
        List<WebElement> cartItemsList = driver.findElements(LocatorRepository.Home.CART_ITEM_CONTAINER);

        for (WebElement item : cartItemsList) {
            if (item.findElement(LocatorRepository.Home.CART_ITEM_TITLE).getText().equals(productName)) {
                int currentQty = Integer.parseInt(item.findElement(LocatorRepository.Home.CART_ITEM_QTY).getText());

                while (currentQty != newQuantity) {
                    final int qtyBeforeClick = currentQty;

                    if (currentQty < newQuantity) {
                        item.findElements(By.tagName("button")).get(1).click();
                    } else {
                        item.findElements(By.tagName("button")).get(0).click();
                    }

                    // Wait for quantity to update
                    wait.until(d -> {
                        try {
                            if (newQuantity == 0 && qtyBeforeClick == 1) return true;
                            int qty = Integer.parseInt(item.findElement(LocatorRepository.Home.CART_ITEM_QTY).getText());
                            return qty != qtyBeforeClick;
                        } catch (Exception e) {
                            return newQuantity == 0; // Item removed
                        }
                    });

                    if (newQuantity == 0 && qtyBeforeClick == 1) break;
                    currentQty = Integer.parseInt(item.findElement(LocatorRepository.Home.CART_ITEM_QTY).getText());
                }
                return this;
            }
        }
        return this;
    }

    /**
     * Clicks the checkout button.
     * @return CheckoutPage instance for chaining
     */
    public CheckoutPage clickCheckout() {
        log.info("Clicking checkout button");
        click(LocatorRepository.Home.CHECKOUT_BUTTON);
        waitForUrlContains(StringConstants.CHECKOUT_ENDPOINT);
        return new CheckoutPage(driver);
    }

    /**
     * Gets the contents of the cart.
     * @return List of product names in the cart
     */
    public List<String> getCartContents() {
        return driver.findElements(LocatorRepository.Home.CART_PRODUCT_TEXT_LIST)
                .stream().map(WebElement::getText).collect(Collectors.toList());
    }

    /**
     * Navigates to the Contact Us section.
     * @return this HomePage instance for chaining
     */
    public HomePage navigateToContactUs() {
        log.info("Navigating to Contact Us");
        click(LocatorRepository.Home.CONTACT_US_LINK);
        wait.until(ExpectedConditions.visibilityOfElementLocated(LocatorRepository.Home.CONTACT_US_MODAL));
        return this;
    }

    /**
     * Clicks the Privacy Policy link.
     * @return this HomePage instance for chaining
     */
    public HomePage clickPrivacyPolicy() {
        log.info("Clicking Privacy Policy link");
        click(LocatorRepository.Home.PRIVACY_POLICY_LINK);
        return this;
    }

    /**
     * Clicks the Terms of Service link.
     * @return this HomePage instance for chaining
     */
    public HomePage clickTermsOfService() {
        log.info("Clicking Terms of Service link");
        click(LocatorRepository.Home.TERMS_OF_SERVICE_LINK);
        return this;
    }
}