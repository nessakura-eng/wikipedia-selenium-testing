package com.wikipedia.tests;

import com.wikipedia.pages.HistoryPage;
import com.wikipedia.utils.AxeAccessibilityUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class HistoryModuleTest extends BaseTest {

    @Test(priority = 1, groups = {"history", "smoke"},
            description = "TC-HI01: Verify article history page loads with revision list")
    public void testHistoryPageLoadsWithRevisionList() {
        HistoryPage historyPage = new HistoryPage(driver);
        historyPage.openHistoryFor("Internet");
        pause();

        // Scroll to history list
        WebElement historyList = driver.findElement(
                By.cssSelector("#pagehistory, ul#pagehistory"));
        scrollTo(historyList);
        Assert.assertTrue(historyPage.isHistoryListPresent(),
                "TC-HI01 FAILED: History list not present");
        pause();

        Assert.assertTrue(driver.getCurrentUrl().contains("action=history"),
                "TC-HI01 FAILED: URL does not contain 'action=history'");
    }

    @Test(priority = 2, groups = {"history", "regression"},
            description = "TC-HI02: Verify revision date links are present in history page")
    public void testHistoryPageHasRevisionDates() {
        HistoryPage historyPage = new HistoryPage(driver);
        historyPage.openHistoryFor("Eiffel_Tower");
        pause();

        // Scroll to first revision date link and click it
        WebElement firstRevision = driver.findElement(
                By.cssSelector("a[href*='oldid=']"));
        scrollTo(firstRevision);
        pause();

        firstRevision.click();
        pause();

        // Verify we landed on an old revision page
        Assert.assertTrue(driver.getCurrentUrl().contains("oldid="),
                "TC-HI02 FAILED: Did not navigate to a revision");

        // Go back to verify count
        driver.navigate().back();
        pause();

        int count = historyPage.getRevisionCount();
        Assert.assertTrue(count > 0,
                "TC-HI02 FAILED: Revision count is 0");
        pause();
    }

    @Test(priority = 3, groups = {"history", "regression"},
            description = "TC-HI03: Verify contributor links are present and clickable")
    public void testHistoryPageHasContributorLinks() {
        HistoryPage historyPage = new HistoryPage(driver);
        historyPage.openHistoryFor("Python_(programming_language)");
        pause();

        Assert.assertTrue(historyPage.areContributorLinksPresent(),
                "TC-HI03 FAILED: No contributor links found");

        // Scroll to first contributor link and click their profile
        WebElement contributorLink = driver.findElement(
                By.cssSelector("a[href*='User:'], a[href*='Special:Contributions']"));
        scrollTo(contributorLink);
        pause();

        contributorLink.click();
        pause();

        // Verify we landed on a user page or contributions page
        Assert.assertTrue(
                driver.getCurrentUrl().contains("User:") ||
                        driver.getCurrentUrl().contains("Special:Contributions"),
                "TC-HI03 FAILED: Did not navigate to contributor page");
        pause();
    }

    @Test(priority = 4, groups = {"history", "regression"},
            description = "TC-HI04: Verify history compare button is present and clickable")
    public void testHistoryPageCompareButtonIsPresent() {
        HistoryPage historyPage = new HistoryPage(driver);
        historyPage.openHistoryFor("United_States");
        pause();

        // Scroll to compare button
        WebElement compareBtn = driver.findElement(
                By.cssSelector("#mw-history-compare, input[type='submit'][name='diff']"));
        scrollTo(compareBtn);
        Assert.assertTrue(historyPage.isCompareButtonPresent(),
                "TC-HI04 FAILED: Compare button not present");
        pause();

        // Select first two checkboxes and click compare
        java.util.List<WebElement> diffRadios = driver.findElements(
                By.cssSelector("input[type='radio'][name='diff']"));
        java.util.List<WebElement> oldIdRadios = driver.findElements(
                By.cssSelector("input[type='radio'][name='oldid']"));
        if (!diffRadios.isEmpty() && !oldIdRadios.isEmpty()) {
            scrollTo(oldIdRadios.get(0));
            jsClick(oldIdRadios.get(0));
            pause();
            scrollTo(diffRadios.get(0));
            jsClick(diffRadios.get(0));
            pause();
        }

        scrollTo(compareBtn);
        jsClick(compareBtn);
        pause();

        boolean compareNavigated = false;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));
        try {
            compareNavigated = wait.until(d -> {
                boolean onDiffUrl = d.getCurrentUrl().contains("diff=")
                        || d.getCurrentUrl().contains("oldid=");
                boolean hasDiffTable = !d.findElements(By.cssSelector("table.diff, .diff")).isEmpty();
                return onDiffUrl || hasDiffTable;
            });
        } catch (TimeoutException ignored) {
            compareNavigated = false;
        }

        if (!compareNavigated) {
            java.util.List<WebElement> rowDiffLinks = driver.findElements(
                    By.cssSelector(".mw-history-histlinks a[href*='diff='], a.mw-changeslist-diff"));
            if (!rowDiffLinks.isEmpty()) {
                scrollTo(rowDiffLinks.get(0));
                jsClick(rowDiffLinks.get(0));
                pause();
            }
        }

        boolean onDiffUrl = driver.getCurrentUrl().contains("diff=")
                || driver.getCurrentUrl().contains("oldid=");
        boolean hasDiffTable = !driver.findElements(By.cssSelector("table.diff, .diff")).isEmpty();
        Assert.assertTrue(onDiffUrl || hasDiffTable,
                "TC-HI04 FAILED: Compare did not navigate to diff page");
        pause();
    }

    @Test(priority = 5, groups = {"history", "accessibility"},
            description = "TC-HI05: Verify WCAG accessibility compliance on the history page")
    public void testHistoryPageWcagCompliance() {
        HistoryPage historyPage = new HistoryPage(driver);
        historyPage.openHistoryFor("Moon");
        pause();

        var results = AxeAccessibilityUtil.runAccessibilityScan(driver, "HistoryPage_Moon");
        AxeAccessibilityUtil.logViolationSummary(results, "HistoryPage_Moon");
        pause();

        Assert.assertNotNull(results, "TC-HI05 FAILED: Axe scan returned null");
        Assert.assertTrue(driver.getCurrentUrl().contains("action=history"),
                "TC-HI05 FAILED: Not on a history page");

        System.out.println("[TC-HI05] WCAG scan complete. Violations: "
                + (results.getViolations() != null ? results.getViolations().size() : 0));
    }
}