package com.qkart.constants;

public class StringConstants {
    private StringConstants() {}

    // URL Endpoints
    public static final String LOGIN_ENDPOINT = "/login";
    public static final String REGISTER_ENDPOINT = "/register";
    public static final String CHECKOUT_ENDPOINT = "/checkout";
    public static final String THANKS_ENDPOINT = "/thanks";

    // Validation Messages
    public static final String INSUFFICIENT_BALANCE_ERR = "You do not have enough balance in your wallet for this purchase";
    public static final String NO_PRODUCTS_FOUND_MSG = " No products found ";
    public static final String PRIVACY_POLICY_TITLE = "Privacy Policy";
    public static final String TERMS_OF_SERVICE_TITLE = "Terms of Service";
}