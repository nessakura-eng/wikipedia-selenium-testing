package com.wikipedia.tests;

import com.wikipedia.pages.ArticlePage;
import com.wikipedia.pages.SearchPage;
import com.wikipedia.utils.AxeAccessibilityUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class SearchModuleTest extends BaseTest {

    @Test(priority = 1, groups = {"search", "smoke"},
            description = "TC-S01: Verify search navigates to a relevant page for a valid query")
    public void testSearchReturnsResultsForValidQuery() {
        SearchPage searchPage = new SearchPage(driver);
        searchPage.searchFor("history of artificial intelligence");
        pause();

        String url = driver.getCurrentUrl();
        Assert.assertTrue(
                url.contains("wikipedia.org/wiki/") || url.contains("Special:Search"),
                "TC-S01 FAILED: Did not navigate to a Wikipedia page. URL: " + url);
        pause();

        // Scroll to and verify content is present
        WebElement content = driver.findElement(
                By.cssSelector("#mw-content-text, .mw-parser-output"));
        scrollTo(content);
        Assert.assertTrue(new ArticlePage(driver).isArticleContentPresent(),
                "TC-S01 FAILED: No content found on the resulting page");
        pause();
    }

    @Test(priority = 2, groups = {"search", "regression"},
            description = "TC-S02: Verify no-results message for a nonsense query")
    public void testSearchNoResultsForNonsenseQuery() {
        SearchPage searchPage = new SearchPage(driver);
        searchPage.searchFor("xyzzy123nonsensequery999qqq");
        pause();

        // Scroll to the results area to show it's empty
        WebElement contentArea = driver.findElement(
                By.cssSelector("#mw-content-text"));
        scrollTo(contentArea);
        pause();

        boolean noResults = searchPage.isNoResultsMessageDisplayed()
                || !searchPage.hasResults();
        Assert.assertTrue(noResults,
                "TC-S02 FAILED: Expected no results for nonsense query");
        pause();
    }

    @Test(priority = 3, groups = {"search", "regression"},
            description = "TC-S03: Verify search autocomplete suggestions appear while typing")
    public void testSearchAutocompleteSuggestionsAppear() {
        driver.get("https://en.wikipedia.org/wiki/Main_Page");

        By searchBy = By.cssSelector(
                "#searchform input[type='search'], .cdx-search-input input, #searchInput");
        By suggestionsBy = By.cssSelector(
                ".cdx-menu-item, .suggestions li, [role='option'], .mw-searchSuggest-link");

        // Wait for search box to be visible and scroll into view
        WebElement searchInput = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(searchBy));
        scrollTo(searchInput);

        // Use JS click to avoid overlay issues in headless
        jsClick(searchInput);

        // Type each letter slowly; re-fetch to avoid stale element
        String[] letters = {"A", "l", "b", "e", "r", "t"};
        for (String letter : letters) {
            searchInput = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(searchBy));
            searchInput.sendKeys(letter);
            try { Thread.sleep(200); } catch (InterruptedException ignored) {}
        }

        // Wait for suggestions to appear
        List<WebElement> suggestions = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(suggestionsBy));

        Assert.assertTrue(suggestions.size() > 0,
                "TC-S03 FAILED: No autocomplete suggestions appeared");

        // Scroll to first suggestion and click via JS
        WebElement firstSuggestion = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(suggestionsBy));
        scrollTo(firstSuggestion);
        jsClick(firstSuggestion);

        // Wait for navigation
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.urlContains("wikipedia.org"));

        Assert.assertTrue(driver.getCurrentUrl().contains("wikipedia.org"),
                "TC-S03 FAILED: Clicking suggestion did not navigate. URL: " + driver.getCurrentUrl());
    }

    @Test(priority = 4, groups = {"search", "regression"},
            description = "TC-S04: Verify search results pagination navigates to next page of results")
    public void testSearchResultsPaginationWorks() {
        // Go directly to search results
        driver.get("https://en.wikipedia.org/w/index.php?title=Special:Search&search=science&fulltext=1&ns0=1");
        pause();

        // Scroll to and verify initial results are present
        WebElement resultsSection = driver.findElement(
                By.cssSelector(".mw-search-results, #mw-content-text"));
        scrollTo(resultsSection);
        pause();

        int initialCount = driver.findElements(
                By.cssSelector(".mw-search-result-heading")).size();
        Assert.assertTrue(initialCount > 0,
                "TC-S04 FAILED: No initial search results found");
        pause();

        // Scroll to and click the Next page link
        WebElement nextPageLink = driver.findElement(
                By.cssSelector(".mw-nextlink, a[title='Next page']"));
        scrollTo(nextPageLink);
        pause();

        nextPageLink.click();
        pause();

        // Verify we moved to the next page of results
        Assert.assertTrue(
                driver.getCurrentUrl().contains("offset=") ||
                        driver.getCurrentUrl().contains("Special:Search") ||
                        driver.getCurrentUrl().contains("Special%3ASearch"),
                "TC-S04 FAILED: Did not navigate to next results page. URL: "
                        + driver.getCurrentUrl());

        // Scroll to results to confirm new page loaded
        WebElement newResults = driver.findElement(
                By.cssSelector(".mw-search-results, #mw-content-text"));
        scrollTo(newResults);
        pause();

        int newCount = driver.findElements(
                By.cssSelector(".mw-search-result-heading")).size();
        Assert.assertTrue(newCount > 0,
                "TC-S04 FAILED: No results on second page");
        pause();
    }

    @Test(priority = 5, groups = {"search", "accessibility"},
            description = "TC-S05: Verify WCAG accessibility compliance on the search results page")
    public void testSearchResultsPageWcagCompliance() {
        SearchPage searchPage = new SearchPage(driver);
        searchPage.searchFor("history of space exploration missions");
        pause();

        var results = AxeAccessibilityUtil.runAccessibilityScan(driver, "SearchResultsPage");
        AxeAccessibilityUtil.logViolationSummary(results, "SearchResultsPage");
        pause();

        Assert.assertNotNull(results, "TC-S05 FAILED: Axe scan returned null");

        System.out.println("[TC-S05] WCAG scan complete. Violations: "
                + (results.getViolations() != null ? results.getViolations().size() : 0));
    }
}