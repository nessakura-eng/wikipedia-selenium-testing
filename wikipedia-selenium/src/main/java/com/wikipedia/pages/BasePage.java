package com.wikipedia.pages;

import com.wikipedia.utils.AxeAccessibilityUtil;
import com.wikipedia.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public abstract class BasePage {

    protected WebDriver driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public void navigateTo(String url) {
        driver.get(url);
    }

    public void scrollDown(int pixels) {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0," + pixels + ")");
    }

    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public boolean isElementDisplayed(By locator) {
        return WaitUtil.isElementPresent(driver, locator);
    }

    /**
     * Runs and logs an Axe WCAG accessibility scan for this page.
     */
    public void runWcagScan(String pageName) {
        var results = AxeAccessibilityUtil.runAccessibilityScan(driver, pageName);
        AxeAccessibilityUtil.logViolationSummary(results, pageName);
    }
}
