package com.qkart.tests;

import com.qkart.config.ConfigManager;
import com.qkart.constants.StringConstants;
import com.qkart.pages.*;
import com.qkart.utils.ExcelUtils;
import com.qkart.utils.WaitUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * QKart Sanity Test Suite.
 * Contains all sanity tests for the QKart e-commerce application.
 */
public class QkartSanityTests extends BaseTest {
    private static final Logger log = LogManager.getLogger(QkartSanityTests.class);

    /**
     * Dynamic DataProvider that reads test data based on test method name.
     * Maps method names to Excel sheet names.
     */
    @DataProvider(name = "testData")
    public Iterator<Object[]> getTestData(Method method) {
        String methodName = method.getName();
        String sheetName = getSheetNameForMethod(methodName);
        log.debug("Loading test data for method: {} from sheet: {}", methodName, sheetName);
        return ExcelUtils.getSheetData(sheetName);
    }

    /**
     * Maps test method names to Excel sheet names.
     */
    private String getSheetNameForMethod(String methodName) {
        switch (methodName) {
            case "testUserRegistrationAndLogin": return "TestCase01";
            case "testReRegistration": return "TestCase02";
            case "testSearchFunctionality": return "TestCase03";
            case "testSizeChart": return "TestCase04";
            case "testHappyFlow": return "TestCase05";
            case "testEditCart": return "TestCase06";
            case "testVerifyCartContentInNewTab": return "TestCase07";
            case "testInsufficientBalance": return "TestCase08";
            case "testContactUs": return "TestCase11";
            case "testAdvertisements": return "TestCase12";
            default: return methodName;
        }
    }

    @Test(description = "TC01: Verify User Registration and Login", dataProvider = "testData")
    public void testUserRegistrationAndLogin(String username, String password) {
        log.info("Starting test: User Registration and Login");

        RegisterPage registerPage = new RegisterPage(getDriver());
        registerPage.navigateToRegisterPage();

        Assert.assertTrue(registerPage.registerUser(username, password, true), "Registration Failed");
        String registeredUser = registerPage.lastGeneratedUsername;

        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.performLogin(registeredUser, password);

        Assert.assertTrue(loginPage.verifyUserLoggedIn(registeredUser), "Login Verification Failed");
        log.info("Test completed successfully");
    }

    @Test(description = "TC02: Verify Re-Registration Fails", dataProvider = "testData")
    public void testReRegistration(String username, String password) {
        log.info("Starting test: Re-Registration");

        RegisterPage registerPage = new RegisterPage(getDriver());
        registerPage.navigateToRegisterPage();
        registerPage.registerUser(username, password, true);
        String registeredUser = registerPage.lastGeneratedUsername;

        registerPage.navigateToRegisterPage();
        boolean success = registerPage.registerUser(registeredUser, password, false);

        Assert.assertFalse(success, "Re-registration should have failed");
        log.info("Test completed successfully");
    }

    @Test(description = "TC03: Verify Search Functionality", dataProvider = "testData")
    public void testSearchFunctionality(String product) {
        log.info("Starting test: Search Functionality for product: {}", product);

        HomePage homePage = new HomePage(getDriver());
        homePage.navigateToHome()
                .searchForProduct(product);

        List<WebElement> results = homePage.getSearchResultElements();
        Assert.assertFalse(results.isEmpty(), "No results found for " + product);
        log.info("Test completed successfully - Found {} results", results.size());
    }

    @Test(description = "TC04: Verify Size Chart", dataProvider = "testData")
    public void testSizeChart(String product) {
        log.info("Starting test: Size Chart for product: {}", product);

        HomePage homePage = new HomePage(getDriver());
        homePage.navigateToHome()
                .searchForProduct(product);

        WebElement firstResult = homePage.getSearchResultElements().get(0);
        SearchResult resultCard = new SearchResult(getDriver(), firstResult);

        Assert.assertTrue(resultCard.verifySizeChartExists(), "Size chart missing for " + product);

        resultCard.openSizeChart();
        List<String> headers = Arrays.asList("Size", "UK/INDIA", "EU", "HEEL TO TOE");
        Assert.assertTrue(resultCard.validateSizeChartContents(headers,
                Arrays.asList(Arrays.asList("6", "6", "40", "9.8"))), "Size chart content mismatch");
        resultCard.closeSizeChart();

        log.info("Test completed successfully");
    }

    @Test(description = "TC05: Happy Flow - Buy Products", dataProvider = "testData")
    public void testHappyFlow(String prod1, String prod2, String address) {
        log.info("Starting test: Happy Flow with products: {}, {}", prod1, prod2);

        // Register and Login
        RegisterPage registerPage = new RegisterPage(getDriver());
        registerPage.navigateToRegisterPage();
        registerPage.registerUser("testUser", "abc@123", true);
        String user = registerPage.lastGeneratedUsername;

        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.performLogin(user, "abc@123");

        // Add products to cart
        HomePage homePage = new HomePage(getDriver());
        homePage.navigateToHome()
                .searchForProduct(prod1)
                .addProductToCart(prod1)
                .searchForProduct(prod2)
                .addProductToCart(prod2);

        // Checkout
        CheckoutPage checkoutPage = homePage.clickCheckout();
        checkoutPage.addNewAddress(address)
                    .selectAddress(address)
                    .placeOrder();

        // Verify order placed
        Assert.assertTrue(checkoutPage.verifyOrderPlacedSuccessfully(), "Order placement failed");
        log.info("Test completed successfully");
    }

    @Test(description = "TC06: Edit Cart Quantity", dataProvider = "testData")
    public void testEditCart(String prod1, String prod2) {
        log.info("Starting test: Edit Cart with products: {}, {}", prod1, prod2);

        // Register and Login
        RegisterPage registerPage = new RegisterPage(getDriver());
        registerPage.navigateToRegisterPage();
        registerPage.registerUser("testUser", "abc@123", true);
        String user = registerPage.lastGeneratedUsername;
        new LoginPage(getDriver()).performLogin(user, "abc@123");

        // Add products and modify cart
        HomePage homePage = new HomePage(getDriver());
        homePage.navigateToHome()
                .searchForProduct(prod1)
                .addProductToCart(prod1)
                .searchForProduct(prod2)
                .addProductToCart(prod2)
                .changeProductQuantityInCart(prod1, 2)
                .changeProductQuantityInCart(prod2, 0)
                .changeProductQuantityInCart(prod1, 1);

        // Checkout
        CheckoutPage checkoutPage = homePage.clickCheckout();
        checkoutPage.addNewAddress("Test Address Edit Cart")
                    .selectAddress("Test Address Edit Cart")
                    .placeOrder();

        Assert.assertTrue(checkoutPage.verifyOrderPlacedSuccessfully(), "Order placement failed");
        log.info("Test completed successfully");
    }

    @Test(description = "TC07: Verify Cart Content in New Tab", dataProvider = "testData")
    public void testVerifyCartContentInNewTab(String productsList) {
        log.info("Starting test: Cart Content in New Tab");

        // Register and Login
        RegisterPage registerPage = new RegisterPage(getDriver());
        registerPage.navigateToRegisterPage();
        registerPage.registerUser("testUser", "abc@123", true);
        String user = registerPage.lastGeneratedUsername;
        new LoginPage(getDriver()).performLogin(user, "abc@123");

        // Add products
        HomePage homePage = new HomePage(getDriver());
        homePage.navigateToHome();

        String[] products = productsList.split(";");
        for (String prod : products) {
            homePage.searchForProduct(prod)
                    .addProductToCart(prod);
        }

        // Open privacy policy in new tab
        homePage.clickPrivacyPolicy();

        // Wait for new tab
        WaitUtils.waitForWindowCount(getDriver(), 2, 10);

        Set<String> handles = getDriver().getWindowHandles();
        String currentHandle = getDriver().getWindowHandle();
        String otherHandle = handles.stream().filter(h -> !h.equals(currentHandle)).findFirst().orElse(null);

        getDriver().switchTo().window(otherHandle);
        getDriver().get(ConfigManager.getUrl());

        // Verify cart contents
        List<String> cartContents = homePage.getCartContents();
        for (String prod : products) {
            Assert.assertTrue(cartContents.contains(prod), "Cart missing product in new tab: " + prod);
        }

        getDriver().close();
        getDriver().switchTo().window(currentHandle);
        log.info("Test completed successfully");
    }

    @Test(description = "TC08: Insufficient Balance Check", dataProvider = "testData")
    public void testInsufficientBalance(String product, String qty) {
        log.info("Starting test: Insufficient Balance with product: {}, qty: {}", product, qty);

        // Register and Login
        RegisterPage registerPage = new RegisterPage(getDriver());
        registerPage.navigateToRegisterPage();
        registerPage.registerUser("testUser", "abc@123", true);
        String user = registerPage.lastGeneratedUsername;
        new LoginPage(getDriver()).performLogin(user, "abc@123");

        // Add product with high quantity
        HomePage homePage = new HomePage(getDriver());
        homePage.navigateToHome()
                .searchForProduct(product)
                .addProductToCart(product)
                .changeProductQuantityInCart(product, Integer.parseInt(qty));

        // Checkout
        CheckoutPage checkoutPage = homePage.clickCheckout();
        checkoutPage.addNewAddress("Test Address Insufficient Balance")
                    .selectAddress("Test Address Insufficient Balance")
                    .placeOrder();

        Assert.assertTrue(checkoutPage.verifyInsufficientBalanceMessage(), "Insufficient balance message not shown");
        log.info("Test completed successfully");
    }

    @Test(description = "TC09: Verify Privacy Policy and Terms of Service", priority = 9)
    public void testPrivacyAndTerms() {
        log.info("Starting test: Privacy Policy and Terms of Service");

        // Register and Login
        RegisterPage registerPage = new RegisterPage(getDriver());
        registerPage.navigateToRegisterPage();
        registerPage.registerUser("testUser", "abc@123", true);
        String user = registerPage.lastGeneratedUsername;
        new LoginPage(getDriver()).performLogin(user, "abc@123");

        HomePage homePage = new HomePage(getDriver());
        homePage.navigateToHome();
        String originalUrl = getDriver().getCurrentUrl();

        homePage.clickPrivacyPolicy();
        Assert.assertEquals(getDriver().getCurrentUrl(), originalUrl, "URL changed on clicking link");

        // Wait for new tab and switch
        WaitUtils.waitForWindowCount(getDriver(), 2, 10);

        Set<String> handles = getDriver().getWindowHandles();
        Object[] handlesArr = handles.toArray();
        getDriver().switchTo().window((String) handlesArr[1]);

        Assert.assertEquals(getDriver().findElement(By.tagName("h2")).getText(),
                StringConstants.PRIVACY_POLICY_TITLE, "Privacy Policy title mismatch");

        getDriver().close();
        getDriver().switchTo().window((String) handlesArr[0]);
        log.info("Test completed successfully");
    }

    @Test(description = "TC11: Contact Us", dataProvider = "testData")
    public void testContactUs(String name, String email, String message) {
        log.info("Starting test: Contact Us with name: {}, email: {}", name, email);

        HomePage homePage = new HomePage(getDriver());
        homePage.navigateToHome()
                .navigateToContactUs();

        ContactUsPage contactPage = new ContactUsPage(getDriver());
        contactPage.fillContactUsForm(name, email, message);

        Assert.assertTrue(contactPage.isContactModalClosed(), "Contact modal did not close");
        log.info("Test completed successfully");
    }

    @Test(description = "TC12: Advertisements", dataProvider = "testData")
    public void testAdvertisements(String product, String address) {
        log.info("Starting test: Advertisements with product: {}", product);

        // Register and Login
        RegisterPage registerPage = new RegisterPage(getDriver());
        registerPage.navigateToRegisterPage();
        registerPage.registerUser("testUser", "abc@123", true);
        String user = registerPage.lastGeneratedUsername;
        new LoginPage(getDriver()).performLogin(user, "abc@123");

        // Add product and checkout
        HomePage homePage = new HomePage(getDriver());
        homePage.navigateToHome()
                .searchForProduct(product)
                .addProductToCart(product);

        CheckoutPage checkoutPage = homePage.clickCheckout();
        checkoutPage.addNewAddress(address)
                    .selectAddress(address)
                    .placeOrder();

        // Wait for thanks page
        WaitUtils.waitForUrlContains(getDriver(), StringConstants.THANKS_ENDPOINT, 10);

        // Verify advertisements
        List<WebElement> ads = getDriver().findElements(By.tagName("iframe"));
        Assert.assertEquals(ads.size(), 3, "Expected 3 Ads");

        getDriver().switchTo().frame(ads.get(0));
        getDriver().findElement(By.xpath("//button[text()='Buy Now']")).click();
        getDriver().switchTo().defaultContent();

        Assert.assertNotEquals(getDriver().getCurrentUrl(),
                ConfigManager.getUrl() + StringConstants.THANKS_ENDPOINT);
        log.info("Test completed successfully");
    }
}

