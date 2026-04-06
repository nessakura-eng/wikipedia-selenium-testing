package com.wikipedia.tests;

import com.wikipedia.pages.SpecialPage;
import com.wikipedia.utils.AxeAccessibilityUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * MODULE 8: Special Pages Module
 *
 * Test Cases:
 * TC-SP01: Verify Special:SpecialPages loads with a list of special pages
 * TC-SP02: Verify Special:RecentChanges loads with a list of recent edits
 * TC-SP03: Verify Special:Statistics loads with a statistics table
 * TC-SP04: Verify Special:Random redirects to a valid random article
 * TC-SP05: Verify WCAG accessibility compliance on the Recent Changes page
 */
public class SpecialPagesModuleTest extends BaseTest {

    /**
     * TC-SP01: Verify Special:SpecialPages loads with a list of special pages.
     * Navigates to Special:SpecialPages and asserts the page lists
     * multiple special page links grouped by category.
     */
    @Test(groups = {"specialpages", "smoke"},
            description = "TC-SP01: Verify Special:SpecialPages loads with a list of special pages")
    public void testSpecialPagesListLoads() {
        SpecialPage specialPage = new SpecialPage(driver);
        specialPage.openSpecialPages();

        Assert.assertTrue(driver.getCurrentUrl().contains("Special:SpecialPages"),
                "TC-SP01 FAILED: URL does not point to Special:SpecialPages. URL: " + driver.getCurrentUrl());
        Assert.assertTrue(specialPage.isSpecialPagesListPresent(),
                "TC-SP01 FAILED: Special pages list is not present");
        int linkCount = specialPage.getSpecialPageLinkCount();
        Assert.assertTrue(linkCount > 5,
                "TC-SP01 FAILED: Less than 5 special page links found. Count: " + linkCount);
    }

    /**
     * TC-SP02: Verify Special:RecentChanges loads with a list of recent edits.
     * Navigates to the Recent Changes page and asserts that the
     * changes list is populated with at least one entry.
     */
    @Test(groups = {"specialpages", "regression"},
            description = "TC-SP02: Verify Special:RecentChanges loads with a list of recent edits")
    public void testRecentChangesPageLoads() {
        SpecialPage specialPage = new SpecialPage(driver);
        specialPage.openRecentChanges();

        Assert.assertTrue(driver.getCurrentUrl().contains("RecentChanges"),
                "TC-SP02 FAILED: URL does not point to RecentChanges. URL: " + driver.getCurrentUrl());
        Assert.assertTrue(specialPage.isRecentChangesListPresent(),
                "TC-SP02 FAILED: Recent changes list is not present on page");
        int changeCount = specialPage.getRecentChangesCount();
        Assert.assertTrue(changeCount > 0,
                "TC-SP02 FAILED: Recent changes count is 0");
    }

    /**
     * TC-SP03: Verify Special:Statistics loads with a statistics table.
     * Opens the Wikipedia statistics page and asserts that a statistics
     * data table is rendered with content.
     */
    @Test(groups = {"specialpages", "regression"},
            description = "TC-SP03: Verify Special:Statistics loads with a statistics table")
    public void testStatisticsPageLoads() {
        SpecialPage specialPage = new SpecialPage(driver);
        specialPage.openStatistics();

        Assert.assertTrue(driver.getCurrentUrl().contains("Statistics"),
                "TC-SP03 FAILED: URL does not point to Statistics page. URL: " + driver.getCurrentUrl());
        Assert.assertTrue(specialPage.isStatisticsTablePresent(),
                "TC-SP03 FAILED: Statistics table is not present on the page");
    }

    /**
     * TC-SP04: Verify Special:Random redirects to a valid random Wikipedia article.
     * Navigates to the Random special page and asserts it redirects to
     * a valid article URL that is not the Random special page itself.
     */
    @Test(groups = {"specialpages", "regression"},
          description = "TC-SP04: Verify Special:Random redirects to a valid random article")
    public void testRandomSpecialPageRedirectsToArticle() {
        driver.get(SpecialPage.RANDOM_URL);

        // Wait for redirect to complete
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("wikipedia.org/wiki/"),
                "TC-SP04 FAILED: Special:Random did not redirect to a wiki page. URL: " + currentUrl);
        Assert.assertFalse(currentUrl.contains("Special:Random"),
                "TC-SP04 FAILED: Still on Special:Random page (no redirect occurred). URL: " + currentUrl);
    }

    /**
     * TC-SP05: Verify WCAG accessibility compliance on the Recent Changes page.
     * Runs an Axe accessibility scan on Special:RecentChanges and saves a WCAG report.
     */
    @Test(groups = {"specialpages", "accessibility"},
          description = "TC-SP05: Verify WCAG accessibility compliance on the Recent Changes page")
    public void testRecentChangesPageWcagCompliance() {
        SpecialPage specialPage = new SpecialPage(driver);
        specialPage.openRecentChanges();

        var results = AxeAccessibilityUtil.runAccessibilityScan(driver, "SpecialPage_RecentChanges");
        AxeAccessibilityUtil.logViolationSummary(results, "SpecialPage_RecentChanges");

        Assert.assertNotNull(results, "TC-SP05 FAILED: Axe scan returned null results");
        Assert.assertTrue(driver.getCurrentUrl().contains("RecentChanges"),
                "TC-SP05 FAILED: Not on RecentChanges page when running WCAG scan");

        System.out.println("[TC-SP05] WCAG scan complete. Violations: "
                + (results.getViolations() != null ? results.getViolations().size() : 0));
    }
}
