package com.qkart.pages;

import com.qkart.constants.LocatorRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * Page Object for Search Result cards.
 * Handles interactions with individual product cards.
 */
public class SearchResult extends BasePage {
    private final WebElement productCard;

    public SearchResult(WebDriver driver, WebElement productCard) {
        super(driver);
        this.productCard = productCard;
    }

    /**
     * Verifies if size chart exists for the product.
     * @return true if size chart button exists
     */
    public boolean verifySizeChartExists() {
        log.debug("Checking if size chart exists for product");
        try {
            return productCard.findElement(LocatorRepository.SearchResultData.SIZE_CHART_BTN).isDisplayed();
        } catch (Exception e) {
            log.debug("Size chart not found");
            return false;
        }
    }

    /**
     * Opens the size chart modal.
     * @return this SearchResult instance for chaining
     */
    public SearchResult openSizeChart() {
        log.info("Opening size chart");
        productCard.findElement(LocatorRepository.SearchResultData.SIZE_CHART_BTN).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(LocatorRepository.SearchResultData.SIZE_CHART_MODAL));
        return this;
    }

    /**
     * Validates size chart contents.
     * @param headers Expected header values
     * @param body Expected body row values
     * @return true if contents match
     */
    public boolean validateSizeChartContents(List<String> headers, List<List<String>> body) {
        log.info("Validating size chart contents");
        try {
            WebElement table = driver.findElement(By.tagName("table"));
            List<WebElement> actualHeaders = table.findElements(LocatorRepository.SearchResultData.TABLE_HEADERS);

            for (int i = 0; i < headers.size(); i++) {
                if (!actualHeaders.get(i).getText().equals(headers.get(i))) {
                    log.warn("Header mismatch at index {}: expected '{}', actual '{}'",
                            i, headers.get(i), actualHeaders.get(i).getText());
                    return false;
                }
            }

            List<WebElement> rows = table.findElements(LocatorRepository.SearchResultData.TABLE_ROWS);
            for (int i = 0; i < body.size(); i++) {
                List<WebElement> cells = rows.get(i).findElements(By.tagName("td"));
                for (int j = 0; j < body.get(i).size(); j++) {
                    if (!cells.get(j).getText().equals(body.get(i).get(j))) {
                        log.warn("Cell mismatch at row {}, col {}: expected '{}', actual '{}'",
                                i, j, body.get(i).get(j), cells.get(j).getText());
                        return false;
                    }
                }
            }
            log.info("Size chart validation passed");
            return true;
        } catch (Exception e) {
            log.error("Size chart validation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Closes the size chart modal.
     * @return this SearchResult instance for chaining
     */
    public SearchResult closeSizeChart() {
        log.info("Closing size chart");
        Actions action = new Actions(driver);
        action.sendKeys(Keys.ESCAPE).perform();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(LocatorRepository.SearchResultData.SIZE_CHART_MODAL));
        return this;
    }
}