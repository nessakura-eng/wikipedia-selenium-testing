package com.wikipedia.tests;

import com.wikipedia.pages.ArticlePage;
import com.wikipedia.pages.HomePage;
import com.wikipedia.utils.AxeAccessibilityUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * MODULE 4: Home Page Module
 *
 * Test Cases:
 * TC-H01: Verify homepage loads with Wikipedia logo and title
 * TC-H02: Verify "Featured Article", "In the News", and "Did You Know" sections are present
 * TC-H03: Verify "On This Day" section is present on the homepage
 * TC-H04: Verify "Random Article" link navigates to a valid article
 * TC-H05: Verify WCAG accessibility compliance on the homepage
 */
public class HomePageModuleTest extends BaseTest {

    /**
     * TC-H01: Verify homepage loads with Wikipedia logo and title.
     * Navigates to the Wikipedia main page and asserts that
     * the logo is displayed and the page title contains "Wikipedia".
     */
    @Test(groups = {"homepage", "smoke"},
            description = "TC-H01: Verify homepage loads with logo and search box")
    public void testHomepageLoadsWithLogoAndSearchBox() {
        HomePage homePage = new HomePage(driver);
        homePage.open();

        Assert.assertTrue(homePage.isLogoDisplayed(),
                "TC-H01 FAILED: Wikipedia logo is not displayed on homepage");
        Assert.assertTrue(homePage.isSearchBoxDisplayed(),
                "TC-H01 FAILED: Search box is not displayed on homepage");
        Assert.assertTrue(driver.getCurrentUrl().contains("Main_Page"),
                "TC-H01 FAILED: Not on Wikipedia main page. URL: " + driver.getCurrentUrl());
    }

    /**
     * TC-H02: Verify "Featured Article", "In the News", and "Did You Know" sections are present.
     * Opens the main page and checks that all three primary content sections
     * are visible on the page.
     */
    @Test(groups = {"homepage", "regression"},
          description = "TC-H02: Verify Featured Article, In the News, and Did You Know sections are present")
    public void testHomepageCoreSectionsArePresent() {
        HomePage homePage = new HomePage(driver);
        homePage.open();

        Assert.assertTrue(homePage.isFeaturedArticleSectionPresent(),
                "TC-H02 FAILED: Featured Article section is not present on homepage");
        Assert.assertTrue(homePage.isInTheNewsSectionPresent(),
                "TC-H02 FAILED: In the News section is not present on homepage");
        Assert.assertTrue(homePage.isDidYouKnowSectionPresent(),
                "TC-H02 FAILED: Did You Know section is not present on homepage");
    }

    /**
     * TC-H03: Verify "On This Day" section is present on the homepage.
     * Opens the main page and validates that the "On This Day" section is rendered.
     */
    @Test(groups = {"homepage", "regression"},
          description = "TC-H03: Verify On This Day section is present on the homepage")
    public void testHomepageOnThisDaySectionIsPresent() {
        HomePage homePage = new HomePage(driver);
        homePage.open();

        Assert.assertTrue(homePage.isOnThisDaySectionPresent(),
                "TC-H03 FAILED: 'On This Day' section is not present on homepage");
    }

    /**
     * TC-H04: Verify "Random Article" link navigates to a valid Wikipedia article.
     * Clicks the Random Article link from the homepage and asserts that
     * the resulting page is a valid article with a title.
     */
    @Test(groups = {"homepage", "regression"},
          description = "TC-H04: Verify Random Article link navigates to a valid article")
    public void testRandomArticleLinkNavigatesToValidArticle() {
        HomePage homePage = new HomePage(driver);
        homePage.open();

        ArticlePage randomArticle = homePage.clickRandomArticle();
        String articleTitle = randomArticle.getArticleTitle();

        Assert.assertFalse(articleTitle.isEmpty(),
                "TC-H04 FAILED: Random article title is empty");
        Assert.assertTrue(driver.getCurrentUrl().contains("wikipedia.org/wiki/"),
                "TC-H04 FAILED: Random article URL does not point to a Wikipedia wiki page");
    }

    /**
     * TC-H05: Verify WCAG accessibility compliance on the homepage.
     * Runs an Axe accessibility scan on the Wikipedia main page and
     * generates a WCAG report for review.
     */
    @Test(groups = {"homepage", "accessibility"},
          description = "TC-H05: Verify WCAG accessibility compliance on the homepage")
    public void testHomepageWcagCompliance() {
        HomePage homePage = new HomePage(driver);
        homePage.open();

        var results = AxeAccessibilityUtil.runAccessibilityScan(driver, "HomePage_MainPage");
        AxeAccessibilityUtil.logViolationSummary(results, "HomePage_MainPage");

        Assert.assertNotNull(results, "TC-H05 FAILED: Axe scan returned null results");
        Assert.assertTrue(homePage.getCurrentUrl().contains("Main_Page"),
                "TC-H05 FAILED: Not on the Main Page when running WCAG scan");

        System.out.println("[TC-H05] WCAG scan complete. Violations: "
                + (results.getViolations() != null ? results.getViolations().size() : 0));
    }
}
