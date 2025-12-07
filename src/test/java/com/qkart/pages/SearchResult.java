package com.qkart.pages;

import com.qkart.constants.LocatorRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;

public class SearchResult extends BasePage {
    private WebElement productCard;

    public SearchResult(WebDriver driver, WebElement productCard) {
        super(driver);
        this.productCard = productCard;
    }

    public boolean verifySizeChartExists() {
        try {
            return productCard.findElement(LocatorRepository.SearchResultData.SIZE_CHART_BTN).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void openSizeChart() {
        productCard.findElement(LocatorRepository.SearchResultData.SIZE_CHART_BTN).click();
        waitForSeconds(2);
    }

    public boolean validateSizeChartContents(List<String> headers, List<List<String>> body) {
        try {
            WebElement table = driver.findElement(By.tagName("table"));
            List<WebElement> actualHeaders = table.findElements(LocatorRepository.SearchResultData.TABLE_HEADERS);
            for (int i = 0; i < headers.size(); i++) {
                if (!actualHeaders.get(i).getText().equals(headers.get(i))) return false;
            }
            List<WebElement> rows = table.findElements(LocatorRepository.SearchResultData.TABLE_ROWS);
            for (int i = 0; i < body.size(); i++) {
                List<WebElement> cells = rows.get(i).findElements(By.tagName("td"));
                for (int j = 0; j < body.get(i).size(); j++) {
                    if (!cells.get(j).getText().equals(body.get(i).get(j))) return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void closeSizeChart() {
        Actions action = new Actions(driver);
        action.sendKeys(Keys.ESCAPE).perform();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(LocatorRepository.SearchResultData.SIZE_CHART_MODAL));
    }
}