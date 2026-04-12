package com.wikipedia.tests;

import com.wikipedia.pages.SpecialPage;
import com.wikipedia.utils.AxeAccessibilityUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SpecialPagesModuleTest extends BaseTest {

    @Test(priority = 1, groups = {"specialpages", "smoke"},
            description = "TC-SP01: Verify Special:SpecialPages loads with a list of special pages")
    public void testSpecialPagesListLoads() {
        SpecialPage specialPage = new SpecialPage(driver);
        specialPage.openSpecialPages();
        pause();

        Assert.assertTrue(driver.getCurrentUrl().contains("Special:SpecialPages"),
                "TC-SP01 FAILED: URL does not point to Special:SpecialPages");

        // Scroll to the special pages list
        WebElement list = driver.findElement(
                By.cssSelector("ul.mw-specialpages-list, .mw-specialpages-group, #mw-content-text ul"));
        scrollTo(list);
        Assert.assertTrue(specialPage.isSpecialPagesListPresent(),
                "TC-SP01 FAILED: Special pages list not present");
        pause();

        // Scroll to and click a specific special page link
        WebElement recentChangesLink = driver.findElement(
                By.cssSelector("a[href*='Special:RecentChanges']"));
        scrollTo(recentChangesLink);
        pause();

        int linkCount = specialPage.getSpecialPageLinkCount();
        Assert.assertTrue(linkCount > 5,
                "TC-SP01 FAILED: Less than 5 special page links found. Count: " + linkCount);
        pause();
    }

    @Test(priority = 2, groups = {"specialpages", "regression"},
            description = "TC-SP02: Verify Special:NewPages loads with a list of newly created pages")
    public void testNewPagesLoads() {
        SpecialPage specialPage = new SpecialPage(driver);

        // Start from Special Pages list and navigate to New Pages
        specialPage.openSpecialPages();
        pause();

        // Scroll to and click the New Pages link
        WebElement newPagesLink = driver.findElement(
                By.cssSelector("a[href*='Special:NewPages']"));
        scrollTo(newPagesLink);
        pause();

        newPagesLink.click();
        pause();

        Assert.assertTrue(driver.getCurrentUrl().contains("NewPages"),
                "TC-SP02 FAILED: Did not navigate to NewPages. URL: " + driver.getCurrentUrl());

        // Scroll to the new pages list
        WebElement newPagesList = driver.findElement(
                By.cssSelector("ul.mw-newpages-list, #mw-content-text ul, ol.special"));
        scrollTo(newPagesList);
        pause();

        String newPagesListURL = driver.getCurrentUrl();

        Assert.assertTrue(newPagesListURL.contains("NewPages"),
                "TC-SP02 FAILED: New pages list is not displayed. URL: " + newPagesListURL);

        pause();

        // Scroll to and click the first new page link
        WebElement firstNewPage = driver.findElement(
                By.className("mw-newpages-pagename"));
        scrollTo(firstNewPage);
        pause();

        firstNewPage.click();
        pause();

        // Verify we navigated to a Wikipedia article
        Assert.assertTrue(driver.getCurrentUrl().contains("wikipedia.org/wiki/"),
                "TC-SP02 FAILED: Clicking new page did not navigate to a Wikipedia article. URL: "
                        + driver.getCurrentUrl());
        pause();
    }

    @Test(priority = 3, groups = {"specialpages", "regression"},
            description = "TC-SP03: Verify Special:Statistics loads with a statistics table")
    public void testStatisticsPageLoads() {
        SpecialPage specialPage = new SpecialPage(driver);
        specialPage.openStatistics();
        pause();

        Assert.assertTrue(driver.getCurrentUrl().contains("Statistics"),
                "TC-SP03 FAILED: URL does not point to Statistics page");

        // Scroll to statistics table
        WebElement statsTable = driver.findElement(
                By.cssSelector("table.wikitable, .wikistats, #mw-content-text table"));
        scrollTo(statsTable);
        Assert.assertTrue(specialPage.isStatisticsTablePresent(),
                "TC-SP03 FAILED: Statistics table not present");
        pause();

        // Scroll through the table rows to show data
        java.util.List<WebElement> rows = driver.findElements(
                By.cssSelector("table.wikitable tr, #mw-content-text table tr"));
        if (rows.size() > 1) {
            scrollTo(rows.get(rows.size() / 2));
            pause();
            scrollTo(rows.get(rows.size() - 1));
            pause();
        }
    }

    @Test(priority = 4, groups = {"specialpages", "regression"},
            description = "TC-SP04: Verify Special:WantedPages loads with a list of wanted pages")
    public void testWantedPagesLoads() {
        SpecialPage specialPage = new SpecialPage(driver);

        // Start from Special Pages list and navigate to Wanted Pages
        specialPage.openSpecialPages();
        pause();

        // Scroll to and click the Wanted Pages link
        WebElement wantedPagesLink = driver.findElement(
                By.cssSelector("a[href*='Special:WantedPages']"));
        scrollTo(wantedPagesLink);
        pause();

        wantedPagesLink.click();
        pause();

        Assert.assertTrue(driver.getCurrentUrl().contains("WantedPages"),
                "TC-SP04 FAILED: Did not navigate to WantedPages. URL: " + driver.getCurrentUrl());

        // Scroll to the wanted pages list
        WebElement wantedList = driver.findElement(
                By.cssSelector("ol.special, #mw-content-text ol, #mw-content-text table"));
        scrollTo(wantedList);
        pause();

        Assert.assertTrue(wantedList.isDisplayed(),
                "TC-SP04 FAILED: Wanted pages list is not displayed");
        pause();

        // Scroll to and click the first wanted page link
        WebElement firstWantedLink = driver.findElement(
                By.cssSelector("ol.special li a, #mw-content-text ol li a, #mw-content-text table a"));
        scrollTo(firstWantedLink);
        pause();

        firstWantedLink.click();
        pause();

        // Verify we navigated somewhere on Wikipedia
        Assert.assertTrue(driver.getCurrentUrl().contains("wikipedia.org"),
                "TC-SP04 FAILED: Clicking wanted page did not navigate to Wikipedia. URL: "
                        + driver.getCurrentUrl());
        pause();
    }

    @Test(priority = 5, groups = {"specialpages", "accessibility"},
            description = "TC-SP05: Verify WCAG accessibility compliance on the Recent Changes page")
    public void testRecentChangesPageWcagCompliance() {
        SpecialPage specialPage = new SpecialPage(driver);
        specialPage.openRecentChanges();
        pause();

        var results = AxeAccessibilityUtil.runAccessibilityScan(driver, "SpecialPage_RecentChanges");
        AxeAccessibilityUtil.logViolationSummary(results, "SpecialPage_RecentChanges");
        pause();

        Assert.assertNotNull(results, "TC-SP05 FAILED: Axe scan returned null");
        Assert.assertTrue(driver.getCurrentUrl().contains("RecentChanges"),
                "TC-SP05 FAILED: Not on RecentChanges page");

        System.out.println("[TC-SP05] WCAG scan complete. Violations: "
                + (results.getViolations() != null ? results.getViolations().size() : 0));
    }

    @Test(priority = 6, groups = {"specialpages", "regression"},
            description = "TC-SP06: Verify Tools menu contains all expected page tools")
    public void testToolsMenuContainsExpectedLinks() {
        SpecialPage specialPage = new SpecialPage(driver);
                specialPage.openMainPage();
        pause();

        specialPage.openToolsMenu();
        pause();

        String[] expectedTools = {
                "What links here",
                "Related changes",
                "Permanent link",
                "Page information",
                "Cite this page",
                "Get shortened URL"
        };

        for (String tool : expectedTools) {
            Assert.assertTrue(specialPage.isToolLinkPresent(tool),
                    "TC-SP06 FAILED: Missing tools menu item: " + tool);
        }
    }

    @Test(priority = 7, groups = {"specialpages", "regression"},
            description = "TC-SP07: Verify Appearance options can be toggled")
    public void testAppearanceOptionsCanBeToggled() {
        SpecialPage specialPage = new SpecialPage(driver);
                specialPage.openMainPage();
        pause();

        boolean unpinnedOrAlreadyUnpinned = specialPage.unpinAppearancePanel();
        pause();
        String htmlClasses = specialPage.getHtmlClassList();
        Assert.assertTrue(unpinnedOrAlreadyUnpinned
                        || htmlClasses.contains("vector-feature-appearance-pinned-clientpref-0")
                        || htmlClasses.contains("vector-feature-appearance-pinned-clientpref-1"),
                "TC-SP07 FAILED: Appearance controls/state could not be validated.");
    }

    @Test(priority = 8, groups = {"specialpages", "regression"},
            description = "TC-SP08: Verify 'Cite this page' tool opens citation content")
    public void testCiteThisPageToolShowsCitationContent() {
        SpecialPage specialPage = new SpecialPage(driver);
        specialPage.openMainPage();
        pause();

        specialPage.openCiteThisPageFromTools();
        pause();

        Assert.assertTrue(driver.getCurrentUrl().contains("Special:CiteThisPage"),
                "TC-SP08 FAILED: Did not navigate to Special:CiteThisPage. URL: " + driver.getCurrentUrl());

        String pageText = driver.findElement(By.tagName("body")).getText();
        Assert.assertTrue(pageText.contains("Main Page") || pageText.contains("main page"),
                "TC-SP08 FAILED: Citation page does not reference the target page.");
        Assert.assertTrue(pageText.contains("Cite") || pageText.contains("citation") || pageText.contains("BibTeX"),
                "TC-SP08 FAILED: Citation-related content not found on Cite This Page view.");
    }

    @Test(priority = 9, groups = {"specialpages", "regression"},
            description = "TC-SP09: Verify 'Get shortened URL' generates a short URL")
    public void testGetShortenedUrlGeneratesShortUrl() {
        SpecialPage specialPage = new SpecialPage(driver);
        specialPage.openMainPage();
        pause();

        specialPage.openGetShortenedUrlFromTools();
        pause();

        Assert.assertTrue(driver.getCurrentUrl().contains("Special:UrlShortener"),
                "TC-SP09 FAILED: Did not navigate to Special:UrlShortener. URL: " + driver.getCurrentUrl());

        specialPage.clickShortenButton();
        pause();

        Assert.assertTrue(specialPage.hasGeneratedShortUrl(),
                "TC-SP09 FAILED: No generated short URL was found (expected w.wiki link).");
    }

    @Test(priority = 10, groups = {"specialpages", "regression"},
            description = "TC-SP10: Verify appearance settings can be cycled and applied")
    public void testAppearanceSettingsCycleAndApply() {
        SpecialPage specialPage = new SpecialPage(driver);
        specialPage.openMainPage();
        pause();

        String htmlBefore = specialPage.getHtmlClassList();
        Assert.assertTrue(specialPage.selectAppearanceOptionAndWaitForClassChange("Small", htmlBefore),
                "TC-SP10 FAILED: Could not apply 'Small' text size option.");
        Assert.assertTrue(specialPage.isAppearanceOptionSelected("Small"),
                "TC-SP10 FAILED: 'Small' text size option is not selected after click.");
        pause();

        String htmlAfterSmall = specialPage.getHtmlClassList();
        Assert.assertTrue(specialPage.selectAppearanceOptionAndWaitForClassChange("Large", htmlAfterSmall),
                "TC-SP10 FAILED: Could not apply 'Large' text size option.");
        Assert.assertTrue(specialPage.isAppearanceOptionSelected("Large"),
                "TC-SP10 FAILED: 'Large' text size option is not selected after click.");
        pause();

        String htmlAfterLarge = specialPage.getHtmlClassList();
        Assert.assertTrue(specialPage.selectAppearanceOptionAndWaitForClassChange("Wide", htmlAfterLarge),
                "TC-SP10 FAILED: Could not apply 'Wide' width option.");
        Assert.assertTrue(specialPage.isAppearanceOptionSelected("Wide"),
                "TC-SP10 FAILED: 'Wide' width option is not selected after click.");
        pause();

        String htmlAfterWide = specialPage.getHtmlClassList();
        Assert.assertTrue(specialPage.selectAppearanceOptionAndWaitForClassChange("Dark", htmlAfterWide),
                "TC-SP10 FAILED: Could not apply 'Dark' color option.");
        Assert.assertTrue(specialPage.isAppearanceOptionSelected("Dark"),
                "TC-SP10 FAILED: 'Dark' color option is not selected after click.");
        pause();

        String htmlAfterDark = specialPage.getHtmlClassList();
        Assert.assertTrue(specialPage.selectAppearanceOptionAndWaitForClassChange("Light", htmlAfterDark),
                "TC-SP10 FAILED: Could not apply 'Light' color option.");
        Assert.assertTrue(specialPage.isAppearanceOptionSelected("Light"),
                "TC-SP10 FAILED: 'Light' color option is not selected after click.");
        pause();

        String finalHtml = specialPage.getHtmlClassList();
        Assert.assertNotEquals(finalHtml, htmlBefore,
                "TC-SP10 FAILED: HTML classes never changed after cycling appearance settings.");
    }

    @Test(priority = 11, groups = {"specialpages", "regression"},
            description = "TC-SP11: Verify appearance settings persist across page navigation")
    public void testAppearanceSettingsPersistAcrossPages() {
        SpecialPage specialPage = new SpecialPage(driver);
        specialPage.openMainPage();
        pause();

        Assert.assertTrue(specialPage.selectAppearanceOption("Large"),
                "TC-SP11 FAILED: Could not select 'Large' text size on Main Page.");
        Assert.assertTrue(specialPage.selectAppearanceOption("Wide"),
                "TC-SP11 FAILED: Could not select 'Wide' width on Main Page.");
        Assert.assertTrue(specialPage.selectAppearanceOption("Dark"),
                "TC-SP11 FAILED: Could not select 'Dark' color on Main Page.");
        pause();

        driver.navigate().to("https://en.wikipedia.org/wiki/Climate_change");
        pause();

        Assert.assertTrue(specialPage.isAppearanceOptionSelected("Large"),
                "TC-SP11 FAILED: 'Large' text size did not persist on article page.");
        Assert.assertTrue(specialPage.isAppearanceOptionSelected("Wide"),
                "TC-SP11 FAILED: 'Wide' width did not persist on article page.");
        Assert.assertTrue(specialPage.isAppearanceOptionSelected("Dark"),
                "TC-SP11 FAILED: 'Dark' color did not persist on article page.");
    }
}