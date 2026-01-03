package com.qkart.tests;

import com.qkart.constants.StringConstants;
import com.qkart.pages.*;
import com.qkart.utils.ConfigLoader;
import com.qkart.utils.ExcelUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class QkartSanityTests extends BaseTest {

    @DataProvider(name = "TC01")
    public Iterator<Object[]> dataTC01() {
        return ExcelUtils.getSheetData("TestCase01");
    }

    @DataProvider(name = "TC02")
    public Iterator<Object[]> dataTC02() {
        return ExcelUtils.getSheetData("TestCase02");
    }

    @DataProvider(name = "TC03")
    public Iterator<Object[]> dataTC03() {
        return ExcelUtils.getSheetData("TestCase03");
    }

    @DataProvider(name = "TC04")
    public Iterator<Object[]> dataTC04() {
        return ExcelUtils.getSheetData("TestCase04");
    }

    @DataProvider(name = "TC05")
    public Iterator<Object[]> dataTC05() {
        return ExcelUtils.getSheetData("TestCase05");
    }

    @DataProvider(name = "TC06")
    public Iterator<Object[]> dataTC06() {
        return ExcelUtils.getSheetData("TestCase06");
    }

    @DataProvider(name = "TC07")
    public Iterator<Object[]> dataTC07() {
        return ExcelUtils.getSheetData("TestCase07");
    }

    @DataProvider(name = "TC08")
    public Iterator<Object[]> dataTC08() {
        return ExcelUtils.getSheetData("TestCase08");
    }

    @DataProvider(name = "TC11")
    public Iterator<Object[]> dataTC11() {
        return ExcelUtils.getSheetData("TestCase11");
    }

    @DataProvider(name = "TC12")
    public Iterator<Object[]> dataTC12() {
        return ExcelUtils.getSheetData("TestCase12");
    }

    @Test(description = "TC01: Verify User Registration and Login", dataProvider = "TC01")
    public void testUserRegistrationAndLogin(String username, String password) {
        RegisterPage registerPage = new RegisterPage(getDriver());
        registerPage.navigateToRegisterPage();

        Assert.assertTrue(registerPage.registerUser(username, password, true), "Registration Failed");
        String registeredUser = registerPage.lastGeneratedUsername;

        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.performLogin(registeredUser, password);

        Assert.assertTrue(loginPage.verifyUserLoggedIn(registeredUser), "Login Verification Failed");
    }

    @Test(description = "TC02: Verify Re-Registration Fails", dataProvider = "TC02")
    public void testReRegistration(String username, String password) {
        RegisterPage registerPage = new RegisterPage(getDriver());
        registerPage.navigateToRegisterPage();
        registerPage.registerUser(username, password, true);
        String registeredUser = registerPage.lastGeneratedUsername;

        registerPage.navigateToRegisterPage();
        boolean success = registerPage.registerUser(registeredUser, password, false);

        Assert.assertFalse(success, "Re-registration should have failed");
    }

    @Test(description = "TC03: Verify Search Functionality", dataProvider = "TC03")
    public void testSearchFunctionality(String product) {
        HomePage homePage = new HomePage(getDriver());
        homePage.navigateToHome();

        homePage.searchForProduct(product);
        List<WebElement> results = homePage.getSearchResultElements();
        Assert.assertTrue(results.size() > 0, "No results found for " + product);
    }

    @Test(description = "TC04: Verify Size Chart", dataProvider = "TC04")
    public void testSizeChart(String product) {
        HomePage homePage = new HomePage(getDriver());
        homePage.navigateToHome();
        homePage.searchForProduct(product);

        WebElement firstResult = homePage.getSearchResultElements().get(0);
        SearchResult resultCard = new SearchResult(getDriver(), firstResult);

        Assert.assertTrue(resultCard.verifySizeChartExists(), "Size chart missing for " + product);

        resultCard.openSizeChart();
        List<String> headers = Arrays.asList("Size", "UK/INDIA", "EU", "HEEL TO TOE");
        Assert.assertTrue(resultCard.validateSizeChartContents(headers, Arrays.asList(Arrays.asList("6", "6", "40", "9.8"))), "Size chart content mismatch");
        resultCard.closeSizeChart();
    }

    @Test(description = "TC05: Happy Flow - Buy Products", dataProvider = "TC05")
    public void testHappyFlow(String prod1, String prod2, String address) throws InterruptedException {
        RegisterPage registerPage = new RegisterPage(getDriver());
        registerPage.navigateToRegisterPage();
        registerPage.registerUser("testUser", "abc@123", true);
        String user = registerPage.lastGeneratedUsername;

        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.performLogin(user, "abc@123");

        HomePage homePage = new HomePage(getDriver());
        homePage.navigateToHome();
        homePage.searchForProduct(prod1);
        homePage.addProductToCart(prod1);
        homePage.searchForProduct(prod2);
        homePage.addProductToCart(prod2);

        homePage.clickCheckout();
        CheckoutPage checkoutPage = new CheckoutPage(getDriver());
        checkoutPage.addNewAddress(address);
        checkoutPage.selectAddress(address);
        checkoutPage.placeOrder();

        Thread.sleep(2000);
        Assert.assertTrue(getDriver().getCurrentUrl().endsWith(StringConstants.THANKS_ENDPOINT), "Order placement failed");
    }

    @Test(description = "TC06: Edit Cart Quantity", dataProvider = "TC06")
    public void testEditCart(String prod1, String prod2) throws InterruptedException {
        RegisterPage registerPage = new RegisterPage(getDriver());
        registerPage.navigateToRegisterPage();
        registerPage.registerUser("testUser", "abc@123", true);
        String user = registerPage.lastGeneratedUsername;
        new LoginPage(getDriver()).performLogin(user, "abc@123");

        HomePage homePage = new HomePage(getDriver());
        homePage.navigateToHome();
        homePage.searchForProduct(prod1);
        homePage.addProductToCart(prod1);
        homePage.searchForProduct(prod2);
        homePage.addProductToCart(prod2);

        homePage.changeProductQuantityInCart(prod1, 2);
        homePage.changeProductQuantityInCart(prod2, 0);
        homePage.changeProductQuantityInCart(prod1, 1);

        homePage.clickCheckout();
        CheckoutPage checkoutPage = new CheckoutPage(getDriver());
        checkoutPage.addNewAddress("Test Address Edit Cart");
        checkoutPage.selectAddress("Test Address Edit Cart");
        checkoutPage.placeOrder();
        Thread.sleep(2000);

        Assert.assertTrue(getDriver().getCurrentUrl().endsWith(StringConstants.THANKS_ENDPOINT));
    }

    @Test(description = "TC07: Verify Cart Content in New Tab", dataProvider = "TC07")
    public void testVerifyCartContentInNewTab(String productsList) {
        RegisterPage registerPage = new RegisterPage(getDriver());
        registerPage.navigateToRegisterPage();
        registerPage.registerUser("testUser", "abc@123", true);
        String user = registerPage.lastGeneratedUsername;
        new LoginPage(getDriver()).performLogin(user, "abc@123");

        HomePage homePage = new HomePage(getDriver());
        homePage.navigateToHome();

        String[] products = productsList.split(";");
        for (String prod : products) {
            homePage.searchForProduct(prod);
            homePage.addProductToCart(prod);
        }

        homePage.clickPrivacyPolicy();
        Set<String> handles = getDriver().getWindowHandles();
        String currentHandle = getDriver().getWindowHandle();
        String otherHandle = handles.stream().filter(h -> !h.equals(currentHandle)).findFirst().orElse(null);

        getDriver().switchTo().window(otherHandle);
        getDriver().get(ConfigLoader.get("url"));

        List<String> cartContents = homePage.getCartContents();
        for (String prod : products) {
            Assert.assertTrue(cartContents.contains(prod), "Cart missing product in new tab: " + prod);
        }

        getDriver().close();
        getDriver().switchTo().window(currentHandle);
    }

    @Test(description = "TC08: Insufficient Balance Check", dataProvider = "TC08")
    public void testInsufficientBalance(String product, String qty) throws InterruptedException {
        RegisterPage registerPage = new RegisterPage(getDriver());
        registerPage.navigateToRegisterPage();
        registerPage.registerUser("testUser", "abc@123", true);
        String user = registerPage.lastGeneratedUsername;
        new LoginPage(getDriver()).performLogin(user, "abc@123");

        HomePage homePage = new HomePage(getDriver());
        homePage.navigateToHome();
        homePage.searchForProduct(product);
        homePage.addProductToCart(product);
        homePage.changeProductQuantityInCart(product, Integer.parseInt(qty));

        homePage.clickCheckout();
        CheckoutPage checkoutPage = new CheckoutPage(getDriver());
        checkoutPage.addNewAddress("Test Address Insufficient Balance");
        checkoutPage.selectAddress("Test Address Insufficient Balance");
        checkoutPage.placeOrder();

        Assert.assertTrue(checkoutPage.verifyInsufficientBalanceMessage(), "Insufficient balance message not shown");
    }

    @Test(description = "TC09: Verify Privacy Policy and Terms of Service (No CSV)", priority = 9)
    public void testPrivacyAndTerms() {
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

        Set<String> handles = getDriver().getWindowHandles();
        Object[] handlesArr = handles.toArray();
        getDriver().switchTo().window((String) handlesArr[1]);

        Assert.assertTrue(getDriver().findElement(By.tagName("h2")).getText().equals(StringConstants.PRIVACY_POLICY_TITLE));
        getDriver().close();
        getDriver().switchTo().window((String) handlesArr[0]);
    }

    @Test(description = "TC11: Contact Us", dataProvider = "TC11")
    public void testContactUs(String name, String email, String message) {
        HomePage homePage = new HomePage(getDriver());
        homePage.navigateToHome();
        homePage.navigateToContactUs();

        ContactUsPage contactPage = new ContactUsPage(getDriver());
        contactPage.fillContactUsForm(name, email, message);

        Assert.assertTrue(contactPage.isContactModalClosed(), "Contact modal did not close");
    }

    @Test(description = "TC12: Advertisements", dataProvider = "TC12")
    public void testAdvertisements(String product, String address) throws InterruptedException {
        RegisterPage registerPage = new RegisterPage(getDriver());
        registerPage.navigateToRegisterPage();
        registerPage.registerUser("testUser", "abc@123", true);
        String user = registerPage.lastGeneratedUsername;
        new LoginPage(getDriver()).performLogin(user, "abc@123");

        HomePage homePage = new HomePage(getDriver());
        homePage.navigateToHome();
        homePage.searchForProduct(product);
        homePage.addProductToCart(product);
        homePage.clickCheckout();

        CheckoutPage checkoutPage = new CheckoutPage(getDriver());
        checkoutPage.addNewAddress(address);
        checkoutPage.selectAddress(address);
        checkoutPage.placeOrder();
        Thread.sleep(2000);

        List<WebElement> ads = getDriver().findElements(By.tagName("iframe"));
        Assert.assertEquals(ads.size(), 3, "Expected 3 Ads");

        getDriver().switchTo().frame(ads.get(0));
        getDriver().findElement(By.xpath("//button[text()='Buy Now']")).click();
        getDriver().switchTo().defaultContent();

        Assert.assertNotEquals(getDriver().getCurrentUrl(), ConfigLoader.get("url") + StringConstants.THANKS_ENDPOINT);
    }
}