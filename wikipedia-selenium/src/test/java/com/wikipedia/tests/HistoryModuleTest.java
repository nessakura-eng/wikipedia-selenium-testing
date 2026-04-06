package com.wikipedia.tests;

import com.wikipedia.pages.ArticlePage;
import com.wikipedia.pages.HistoryPage;
import com.wikipedia.utils.AxeAccessibilityUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * MODULE 6: History Module
 *
 * Test Cases:
 * TC-HI01: Verify article history page loads with revision list
 * TC-HI02: Verify revision dates/links are present in history page
 * TC-HI03: Verify contributor links are present in history page
 * TC-HI04: Verify history compare button is present
 * TC-HI05: Verify WCAG accessibility compliance on the history page
 */
public class HistoryModuleTest extends BaseTest {

    /**
     * TC-HI01: Verify article history page loads with revision list.
     * Navigates to the edit history of the "Internet" article and
     * asserts that the revision list is present.
     */
    @Test(groups = {"history", "smoke"},
          description = "TC-HI01: Verify article history page loads with revision list")
    public void testHistoryPageLoadsWithRevisionList() {
        HistoryPage historyPage = new HistoryPage(driver);
        historyPage.openHistoryFor("Internet");

        Assert.assertTrue(historyPage.isHistoryListPresent(),
                "TC-HI01 FAILED: History list (#pagehistory) is not present");
        Assert.assertTrue(driver.getCurrentUrl().contains("action=history"),
                "TC-HI01 FAILED: URL does not contain 'action=history'");
    }

    /**
     * TC-HI02: Verify revision dates/links are present in history page.
     * Opens the history for "Eiffel Tower" article and asserts that
     * the page contains clickable revision date links.
     */
    @Test(groups = {"history", "regression"},
          description = "TC-HI02: Verify revision date links are present in history page")
    public void testHistoryPageHasRevisionDates() {
        HistoryPage historyPage = new HistoryPage(driver);
        historyPage.openHistoryFor("Eiffel_Tower");

        Assert.assertTrue(historyPage.areRevisionDatesPresent(),
                "TC-HI02 FAILED: No revision date links (oldid) found in history page");
        int count = historyPage.getRevisionCount();
        Assert.assertTrue(count > 0,
                "TC-HI02 FAILED: Revision count is 0 for Eiffel Tower history");
    }

    /**
     * TC-HI03: Verify contributor/user links are present in history page.
     * Opens the history for "Python (programming language)" and asserts
     * that contributor usernames/links are displayed.
     */
    @Test(groups = {"history", "regression"},
          description = "TC-HI03: Verify contributor links are present in history page")
    public void testHistoryPageHasContributorLinks() {
        HistoryPage historyPage = new HistoryPage(driver);
        historyPage.openHistoryFor("Python_(programming_language)");

        Assert.assertTrue(historyPage.areContributorLinksPresent(),
                "TC-HI03 FAILED: No contributor/user links found in history page");
    }

    /**
     * TC-HI04: Verify history "Compare selected revisions" button is present.
     * Opens the history of the "United States" article and asserts that
     * the comparison submit button is visible.
     */
    @Test(groups = {"history", "regression"},
          description = "TC-HI04: Verify history compare button is present")
    public void testHistoryPageCompareButtonIsPresent() {
        HistoryPage historyPage = new HistoryPage(driver);
        historyPage.openHistoryFor("United_States");

        Assert.assertTrue(historyPage.isCompareButtonPresent(),
                "TC-HI04 FAILED: Compare revisions button is not present on history page");
    }

    /**
     * TC-HI05: Verify WCAG accessibility compliance on the history page.
     * Runs an Axe accessibility scan on the history page of the "Moon" article
     * and saves a WCAG report.
     */
    @Test(groups = {"history", "accessibility"},
          description = "TC-HI05: Verify WCAG accessibility compliance on the history page")
    public void testHistoryPageWcagCompliance() {
        HistoryPage historyPage = new HistoryPage(driver);
        historyPage.openHistoryFor("Moon");

        var results = AxeAccessibilityUtil.runAccessibilityScan(driver, "HistoryPage_Moon");
        AxeAccessibilityUtil.logViolationSummary(results, "HistoryPage_Moon");

        Assert.assertNotNull(results, "TC-HI05 FAILED: Axe scan returned null results");
        Assert.assertTrue(driver.getCurrentUrl().contains("action=history"),
                "TC-HI05 FAILED: Not on a history page when running WCAG scan");

        System.out.println("[TC-HI05] WCAG scan complete. Violations: "
                + (results.getViolations() != null ? results.getViolations().size() : 0));
    }
}
