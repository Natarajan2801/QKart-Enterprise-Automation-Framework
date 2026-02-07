package com.qkart.constants;

import org.openqa.selenium.By;

public class LocatorRepository {
    private LocatorRepository() {}

    public static class Register {
        public static final By USERNAME_INPUT = By.id("username");
        public static final By PASSWORD_INPUT = By.id("password");
        public static final By CONFIRM_PASSWORD_INPUT = By.id("confirmPassword");
        public static final By REGISTER_BUTTON = By.className("button");
    }

    public static class Login {
        public static final By USERNAME_INPUT = By.id("username");
        public static final By PASSWORD_INPUT = By.id("password");
        public static final By LOGIN_BUTTON = By.className("button");
        public static final By USERNAME_LABEL = By.className("username-text");
    }

    public static class Home {
        public static final By LOGOUT_BUTTON = By.xpath("//button[text()='Logout']");
        public static final By SEARCH_BOX = By.name("search");
        public static final By SEARCH_RESULTS = By.className("css-1qw96cp");
        public static final By NO_RESULTS_MSG = By.xpath("//h4[contains(text(),'No products found')]");
        public static final By CHECKOUT_BUTTON = By.className("checkout-btn");
        public static final By CONTACT_US_LINK = By.xpath("//*[text()='Contact us']");
        public static final By PRIVACY_POLICY_LINK = By.linkText("Privacy policy");
        public static final By TERMS_OF_SERVICE_LINK = By.linkText("Terms of Service");
        public static final By CONTACT_US_MODAL = By.xpath("//div[contains(@class,'card-block')]");

        public static final By CART_ITEM_CONTAINER = By.className("css-zgtx0t");
        public static final By CART_ITEM_TITLE = By.xpath(".//div[contains(@class,'css-1gjj37g')]/div[1]");
        public static final By CART_ITEM_QTY = By.className("css-olyig7");
        public static final By CART_PRODUCT_TEXT_LIST = By.xpath("//div[contains(@class,'cart')]//div[@class='MuiBox-root css-1gjj37g']/div[1]");

        public static final String PRODUCT_ADD_BUTTON_XPATH = "//p[text()='%s']/../..//button";
        public static final String CART_ITEM_PRESENCE_XPATH = "//div[contains(@class,'cart')]//div[text()='%s']";
    }

    public static class Checkout {
        public static final By ADD_NEW_ADDRESS_BTN = By.id("add-new-btn");
        public static final By ADDRESS_INPUT_BOX = By.className("MuiOutlinedInput-input");
        public static final By ADD_ADDRESS_SAVE_BTN = By.xpath("//button[text()='Add']");
        public static final By PLACE_ORDER_BTN = By.xpath("//button[text()='PLACE ORDER']");
        public static final By SNACKBAR_MESSAGE = By.id("notistack-snackbar");

        public static final String ADDRESS_TEXT_XPATH = "//p[text()='%s']";
        public static final String ADDRESS_RADIO_BTN_XPATH = "//p[text()='%s']/ancestor::div[contains(@class,'address-item')]//input";
    }

    public static class SearchResultData {
        public static final By SIZE_CHART_BTN = By.xpath(".//button[text()='Size chart']");
        public static final By TABLE_HEADERS = By.xpath(".//thead//th");
        public static final By TABLE_ROWS = By.xpath(".//tbody//tr");
        public static final By SIZE_CHART_MODAL = By.className("MuiDialog-paperScrollPaper");
    }

    public static class ContactUs {
        public static final By NAME_INPUT = By.xpath("//input[@placeholder='Name']");
        public static final By EMAIL_INPUT = By.xpath("//input[@placeholder='Email']");
        public static final By MESSAGE_INPUT = By.xpath("//input[@placeholder='Message']");
        public static final By CONTACT_NOW_BTN = By.xpath("//button[text()=' Contact Now']");
    }
}