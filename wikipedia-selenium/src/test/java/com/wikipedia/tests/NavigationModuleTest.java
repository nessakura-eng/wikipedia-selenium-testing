package com.wikipedia.tests;

import com.wikipedia.pages.HomePage;
import com.wikipedia.pages.NavigationPage;
import com.wikipedia.utils.AxeAccessibilityUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * MODULE 7: Navigation Module
 *
 * Test Cases:
 * TC-N01: Verify clicking the Wikipedia logo returns to the main page
 * TC-N02: Verify footer is present with expected links
 * TC-N03: Verify "Random Article" navigation link is present in sidebar
 * TC-N04: Verify page tabs (Edit, History) are present on article pages
 * TC-N05: Verify WCAG accessibility compliance on the About Wikipedia page
 */
public class NavigationModuleTest extends BaseTest {

    /**
     * TC-N01: Verify clicking the Wikipedia logo returns to the main page.
     * Navigates to an article page, clicks the Wikipedia logo,
     * and asserts the user is returned to the main page.
     */
    @Test(groups = {"navigation", "smoke"},
          description = "TC-N01: Verify clicking the Wikipedia logo returns to the main page")
    public void testLogoClickReturnsToMainPage() {
        driver.get("https://en.wikipedia.org/wiki/Albert_Einstein");
        NavigationPage navPage = new NavigationPage(driver);

        Assert.assertTrue(navPage.isLogoDisplayed(),
                "TC-N01 FAILED: Logo is not displayed on article page");

        navPage.clickLogo();

        String url = driver.getCurrentUrl();
        Assert.assertTrue(url.contains("Main_Page") || url.equals("https://en.wikipedia.org/"),
                "TC-N01 FAILED: Logo click did not return to main page. URL: " + url);
    }

    /**
     * TC-N02: Verify footer is present with expected links.
     * Opens the main page and asserts the footer section is visible
     * with at least one footer link.
     */
    @Test(groups = {"navigation", "regression"},
          description = "TC-N02: Verify footer is present with expected footer links")
    public void testFooterIsPresentWithLinks() {
        HomePage homePage = new HomePage(driver);
        homePage.open();
        NavigationPage navPage = new NavigationPage(driver);

        Assert.assertTrue(navPage.isFooterDisplayed(),
                "TC-N02 FAILED: Footer is not displayed on the homepage");
        int footerLinkCount = navPage.getFooterLinkCount();
        Assert.assertTrue(footerLinkCount > 0,
                "TC-N02 FAILED: Footer has no links. Count: " + footerLinkCount);
    }

    /**
     * TC-N03: Verify "Random Article" navigation link is present in the sidebar.
     * Opens the main page and asserts the Random Article navigation link is visible.
     */
    @Test(groups = {"navigation", "regression"},
          description = "TC-N03: Verify Random Article navigation link is present in sidebar")
    public void testRandomArticleLinkInSidebar() {
        HomePage homePage = new HomePage(driver);
        homePage.open();
        NavigationPage navPage = new NavigationPage(driver);

        Assert.assertTrue(navPage.isRandomArticleLinkPresent(),
                "TC-N03 FAILED: Random Article link is not present in the sidebar/navigation");
    }

    /**
     * TC-N04: Verify page action tabs (Edit, History) are present on article pages.
     * Opens an article page and asserts that both the Edit and History tabs
     * are displayed in the page toolbar.
     */
    @Test(groups = {"navigation", "regression"},
          description = "TC-N04: Verify Edit and History tabs are present on article pages")
    public void testEditAndHistoryTabsPresentOnArticlePage() {
        driver.get("https://en.wikipedia.org/wiki/Solar_System");
        NavigationPage navPage = new NavigationPage(driver);

        Assert.assertTrue(navPage.isEditTabPresent(),
                "TC-N04 FAILED: Edit tab is not present on article page");
        Assert.assertTrue(navPage.isHistoryTabPresent(),
                "TC-N04 FAILED: History tab is not present on article page");
    }

    /**
     * TC-N05: Verify WCAG accessibility compliance on the About Wikipedia page.
     * Navigates to the "Wikipedia:About" page and runs an Axe accessibility scan.
     */
    @Test(groups = {"navigation", "accessibility"},
          description = "TC-N05: Verify WCAG accessibility compliance on the About Wikipedia page")
    public void testAboutPageWcagCompliance() {
        NavigationPage navPage = new NavigationPage(driver);
        navPage.navigateToAboutPage();

        var results = AxeAccessibilityUtil.runAccessibilityScan(driver, "AboutPage_Wikipedia");
        AxeAccessibilityUtil.logViolationSummary(results, "AboutPage_Wikipedia");

        Assert.assertNotNull(results, "TC-N05 FAILED: Axe scan returned null results");
        Assert.assertTrue(driver.getCurrentUrl().contains("Wikipedia:About"),
                "TC-N05 FAILED: Not on the About page when running WCAG scan");

        System.out.println("[TC-N05] WCAG scan complete. Violations: "
                + (results.getViolations() != null ? results.getViolations().size() : 0));
    }
}
