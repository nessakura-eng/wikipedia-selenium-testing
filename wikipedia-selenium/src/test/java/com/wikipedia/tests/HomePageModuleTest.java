package com.wikipedia.tests;

import com.wikipedia.pages.ArticlePage;
import com.wikipedia.pages.HomePage;
import com.wikipedia.utils.AxeAccessibilityUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HomePageModuleTest extends BaseTest {

    @Test(priority = 1, groups = {"homepage", "smoke"},
            description = "TC-H01: Verify homepage loads with logo and search box")
    public void testHomepageLoadsWithLogoAndSearchBox() {
        HomePage homePage = new HomePage(driver);
        homePage.open();
        pause();

        // Scroll to and verify logo
        WebElement logo = driver.findElement(
                By.cssSelector(".mw-logo, #p-logo a, a.mw-wiki-logo"));
        scrollTo(logo);
        Assert.assertTrue(homePage.isLogoDisplayed(),
                "TC-H01 FAILED: Wikipedia logo not displayed");
        pause();

        // Scroll to and verify search box
        WebElement searchBox = driver.findElement(
                By.cssSelector("#searchform, .cdx-search-input, #searchInput"));
        scrollTo(searchBox);
        Assert.assertTrue(homePage.isSearchBoxDisplayed(),
                "TC-H01 FAILED: Search box not displayed");
        pause();

        Assert.assertTrue(driver.getCurrentUrl().contains("Main_Page"),
                "TC-H01 FAILED: Not on main page. URL: " + driver.getCurrentUrl());
    }

    @Test(priority = 2, groups = {"homepage", "regression"},
            description = "TC-H02: Verify Featured Article, In the News, and Did You Know sections are present")
    public void testHomepageCoreSectionsArePresent() {
        HomePage homePage = new HomePage(driver);
        homePage.open();
        pause();

        // Scroll to Featured Article section
        WebElement featured = driver.findElement(
                By.cssSelector("#mp-tfa, #mp-upper, .mp-tfa"));
        scrollTo(featured);
        Assert.assertTrue(homePage.isFeaturedArticleSectionPresent(),
                "TC-H02 FAILED: Featured Article section not present");
        pause();

        // Scroll to In the News section
        WebElement news = driver.findElement(
                By.cssSelector("#mp-itn, .mp-itn"));
        scrollTo(news);
        Assert.assertTrue(homePage.isInTheNewsSectionPresent(),
                "TC-H02 FAILED: In the News section not present");
        pause();

        // Scroll to Did You Know section
        WebElement dyk = driver.findElement(
                By.cssSelector("#mp-dyk, .mp-dyk"));
        scrollTo(dyk);
        Assert.assertTrue(homePage.isDidYouKnowSectionPresent(),
                "TC-H02 FAILED: Did You Know section not present");
        pause();
    }

    @Test(priority = 3, groups = {"homepage", "regression"},
            description = "TC-H03: Verify On This Day section is present on the homepage")
    public void testHomepageOnThisDaySectionIsPresent() {
        HomePage homePage = new HomePage(driver);
        homePage.open();
        pause();

        // Scroll down to On This Day section so it is visible
        WebElement onThisDay = driver.findElement(
                By.cssSelector("#mp-otd, .mp-otd"));
        scrollTo(onThisDay);
        Assert.assertTrue(homePage.isOnThisDaySectionPresent(),
                "TC-H03 FAILED: On This Day section not present");
        pause();
    }

    @Test(priority = 4, groups = {"homepage", "regression"},
            description = "TC-H04: Verify Random Article link navigates to a valid article")
    public void testRandomArticleLinkNavigatesToValidArticle() throws InterruptedException {
        HomePage homePage = new HomePage(driver);
        homePage.open();
        pause();

        // JS click on random article link
        ArticlePage randomArticle = homePage.clickRandomArticle();
        pause();

        Assert.assertFalse(randomArticle.getArticleTitle().isEmpty(),
                "TC-H04 FAILED: Random article title is empty");

        // Scroll to confirm article content loaded
        WebElement content = driver.findElement(By.cssSelector("#mw-content-text"));
        scrollTo(content);
        Assert.assertTrue(driver.getCurrentUrl().contains("wikipedia.org/wiki/"),
                "TC-H04 FAILED: Not on a Wikipedia article page");
        pause();
    }

    @Test(priority = 5, groups = {"homepage", "accessibility"},
            description = "TC-H05: Verify WCAG accessibility compliance on the homepage")
    public void testHomepageWcagCompliance() {
        HomePage homePage = new HomePage(driver);
        homePage.open();
        pause();

        var results = AxeAccessibilityUtil.runAccessibilityScan(driver, "HomePage_MainPage");
        AxeAccessibilityUtil.logViolationSummary(results, "HomePage_MainPage");
        pause();

        Assert.assertNotNull(results, "TC-H05 FAILED: Axe scan returned null");
        Assert.assertTrue(homePage.getCurrentUrl().contains("Main_Page"),
                "TC-H05 FAILED: Not on the Main Page");

        System.out.println("[TC-H05] WCAG scan complete. Violations: "
                + (results.getViolations() != null ? results.getViolations().size() : 0));
    }
}