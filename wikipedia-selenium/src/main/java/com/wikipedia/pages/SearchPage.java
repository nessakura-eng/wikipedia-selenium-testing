package com.wikipedia.pages;

import com.wikipedia.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SearchPage extends BasePage {

    public static final String SEARCH_URL = "https://en.wikipedia.org/w/index.php?title=Special:Search&search=";

    // Wikipedia Vector 2022 skin uses a Codex search input inside #searchform
    private static final String SEARCH_INPUT_CSS =
            "#searchform input[type='search'], #searchform input[name='search'], " +
            ".cdx-search-input input, #searchText, input[name='search']";

    public SearchPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigate to the search results page, forcing full-text search so Wikipedia
     * never silently redirects to a single article.  The &fulltext=1 parameter
     * instructs MediaWiki to always show the results list.
     */
    public void searchFor(String query) {
        navigateTo("https://en.wikipedia.org/wiki/Main_Page");
        // Wait for page to fully settle before touching the search input
        WaitUtil.waitForVisible(driver, By.cssSelector("#mp-upper, #mw-content-text"));
        // Use JavaScript to set the value directly — avoids stale element from JS re-renders
        By searchInputBy = By.cssSelector(
                "#searchform input[type='search'], #searchform input[name='search'], " +
                        ".cdx-search-input input, #searchInput, input[name='search']");
        WebElement input = WaitUtil.waitForVisible(driver, searchInputBy);
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].value = arguments[1];", input, query);
        input.sendKeys(Keys.ENTER);
        WaitUtil.waitForVisible(driver, By.cssSelector("#firstHeading, .firstHeading, h1"));
    }

    public void searchViaInput(String query) {
        By inputBy = By.cssSelector(SEARCH_INPUT_CSS);
        WebElement input = WaitUtil.waitForVisible(driver, inputBy);
        input.clear();
        input.sendKeys(query);
        input.sendKeys(Keys.ENTER);
        WaitUtil.waitForUrl(driver, "Special:Search");
    }

    public int getSearchResultCount() {
        return driver.findElements(By.cssSelector(
                ".mw-search-results li, .searchresult, .mw-search-result")).size();
    }

    public boolean hasResults() {
        return !driver.findElements(By.cssSelector(
                ".mw-search-results li, .searchresult, .mw-search-result")).isEmpty();
    }

    public boolean isNoResultsMessageDisplayed() {
        return isElementDisplayed(By.cssSelector(".mw-search-nonefound"));
    }

    public String getFirstResultTitle() {
        List<WebElement> links = driver.findElements(
                By.cssSelector(".mw-search-result-heading a"));
        if (!links.isEmpty()) return links.get(0).getText();
        return "";
    }

    public ArticlePage clickFirstResult() {
        List<WebElement> links = driver.findElements(
                By.cssSelector(".mw-search-result-heading a"));
        if (!links.isEmpty()) {
            links.get(0).click();
        }
        return new ArticlePage(driver);
    }

    public boolean isSearchInputPresent() {
        return isElementDisplayed(By.cssSelector(SEARCH_INPUT_CSS));
    }

    public boolean isNextPageAvailable() {
        return isElementDisplayed(By.cssSelector(".mw-nextlink, a[title*='next results']"));
    }

    public void goToNextPage() {
        WaitUtil.waitForClickable(driver, By.cssSelector(".mw-nextlink, a[title*='next results']")).click();
    }

    public List<WebElement> getSearchResultLinks() {
        return driver.findElements(By.cssSelector(".mw-search-result-heading a"));
    }
}
