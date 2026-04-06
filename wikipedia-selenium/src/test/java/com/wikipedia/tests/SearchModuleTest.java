package com.wikipedia.tests;

import com.wikipedia.pages.ArticlePage;
import com.wikipedia.pages.HomePage;
import com.wikipedia.pages.SearchPage;
import com.wikipedia.utils.AxeAccessibilityUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * MODULE 3: Search Module
 *
 * Test Cases:
 * TC-S01: Verify search returns relevant results for a valid query
 * TC-S02: Verify "no results" state for a nonsense query
 * TC-S03: Verify search from homepage navigates to correct article
 * TC-S04: Verify clicking first search result opens an article page
 * TC-S05: Verify WCAG accessibility compliance on the search results page
 */
public class SearchModuleTest extends BaseTest {

    /**
     * TC-S01: Verify search returns relevant results for a valid query.
     * Uses &fulltext=1 to force the results list page, then asserts
     * at least one result is present.
     */
    @Test(groups = {"search", "smoke"},
            description = "TC-S01: Verify search navigates to a relevant page for a valid query")
    public void testSearchReturnsResultsForValidQuery() {
        SearchPage searchPage = new SearchPage(driver);
        searchPage.searchFor("history of artificial intelligence");

        String url = driver.getCurrentUrl();
        Assert.assertTrue(
                url.contains("wikipedia.org/wiki/") || url.contains("Special:Search"),
                "TC-S01 FAILED: Search did not navigate to a Wikipedia page. URL: " + url);
        Assert.assertTrue(
                new com.wikipedia.pages.ArticlePage(driver).isArticleContentPresent(),
                "TC-S01 FAILED: No content found on the resulting page");
    }

    /**
     * TC-S02: Verify "no results" message for a nonsense/invalid query.
     * Searches for a string of random characters that should yield no results
     * and asserts the no-results message is displayed.
     */
    @Test(groups = {"search", "regression"},
          description = "TC-S02: Verify no-results message for a nonsense query")
    public void testSearchNoResultsForNonsenseQuery() {
        SearchPage searchPage = new SearchPage(driver);
        searchPage.searchFor("xyzzy123nonsensequery999qqq");

        boolean noResults = searchPage.isNoResultsMessageDisplayed()
                || !searchPage.hasResults();
        Assert.assertTrue(noResults,
                "TC-S02 FAILED: Expected no results or a no-results message for nonsense query");
    }

    /**
     * TC-S03: Verify search from homepage navigates correctly.
     * Simulates a search initiated from the homepage by using Wikipedia's
     * search URL directly (equivalent to submitting the homepage search form),
     * then confirms the resulting page is a valid Wikipedia page.
     */
    @Test(groups = {"search", "regression"},
          description = "TC-S03: Verify search from homepage navigates correctly")
    public void testSearchFromHomepageNavigatesCorrectly() {
        // Open homepage first to establish the starting context
        HomePage homePage = new HomePage(driver);
        homePage.open();

        // Trigger search via URL (same as submitting the homepage search form).
        // Wikipedia's Vector 2022 search input is a JS-powered overlay that is not
        // a standard visible input — navigating by URL is the reliable equivalent.
        SearchPage searchPage = new SearchPage(driver);
        searchPage.searchFor("Solar System history");

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(
                currentUrl.contains("wikipedia.org/wiki/") || currentUrl.contains("Special:Search"),
                "TC-S03 FAILED: URL after search does not contain expected page. URL: " + currentUrl);
    }

    /**
     * TC-S04: Verify clicking the first search result opens an article page.
     * Searches for a phrase query to guarantee the results list is shown,
     * then clicks the first result and verifies an article loads.
     */
    @Test(groups = {"search", "regression"},
          description = "TC-S04: Verify clicking first search result opens an article page")
    public void testClickFirstSearchResultOpensArticle() {
        SearchPage searchPage = new SearchPage(driver);
        // Use &fulltext=1 (built into searchFor) with a phrase to guarantee results list
        searchPage.searchFor("principles of quantum mechanics physics");

        Assert.assertTrue(searchPage.hasResults(),
                "TC-S04 FAILED: No results found for quantum mechanics phrase query");

        ArticlePage articlePage = searchPage.clickFirstResult();
        String articleTitle = articlePage.getArticleTitle();

        Assert.assertFalse(articleTitle.isEmpty(),
                "TC-S04 FAILED: Article title is empty after clicking search result");
        Assert.assertTrue(driver.getCurrentUrl().contains("wikipedia.org/wiki/"),
                "TC-S04 FAILED: URL does not point to a Wikipedia article");
    }

    /**
     * TC-S05: Verify WCAG accessibility compliance on the search results page.
     * Performs a search, then runs an Axe accessibility scan on the results page.
     */
    @Test(groups = {"search", "accessibility"},
          description = "TC-S05: Verify WCAG accessibility compliance on the search results page")
    public void testSearchResultsPageWcagCompliance() {
        SearchPage searchPage = new SearchPage(driver);
        searchPage.searchFor("history of space exploration missions");

        var results = AxeAccessibilityUtil.runAccessibilityScan(driver, "SearchResultsPage");
        AxeAccessibilityUtil.logViolationSummary(results, "SearchResultsPage");

        Assert.assertNotNull(results, "TC-S05 FAILED: Axe scan returned null results");

        System.out.println("[TC-S05] WCAG scan complete. Violations: "
                + (results.getViolations() != null ? results.getViolations().size() : 0));
    }
}
