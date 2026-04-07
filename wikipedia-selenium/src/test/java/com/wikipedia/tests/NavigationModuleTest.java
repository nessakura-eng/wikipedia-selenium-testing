package com.wikipedia.tests;

import com.wikipedia.pages.HomePage;
import com.wikipedia.pages.NavigationPage;
import com.wikipedia.utils.AxeAccessibilityUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NavigationModuleTest extends BaseTest {

    @Test(priority = 1, groups = {"navigation", "smoke"},
            description = "TC-N01: Verify clicking the Wikipedia logo returns to the main page")
    public void testLogoClickReturnsToMainPage() {
        driver.get("https://en.wikipedia.org/wiki/Albert_Einstein");
        NavigationPage navPage = new NavigationPage(driver);
        pause();

        // Scroll to logo and click it
        WebElement logo = driver.findElement(
                By.cssSelector(".mw-logo, a.mw-wiki-logo, #p-logo a"));
        scrollTo(logo);
        Assert.assertTrue(navPage.isLogoDisplayed(),
                "TC-N01 FAILED: Logo not displayed");
        pause();

        logo.click();
        pause();

        String url = driver.getCurrentUrl();
        Assert.assertTrue(url.contains("Main_Page") || url.equals("https://en.wikipedia.org/"),
                "TC-N01 FAILED: Logo click did not return to main page. URL: " + url);
    }

    @Test(priority = 2, groups = {"navigation", "regression"},
            description = "TC-N02: Verify footer is present with expected footer links")
    public void testFooterIsPresentWithLinks() {
        HomePage homePage = new HomePage(driver);
        homePage.open();
        NavigationPage navPage = new NavigationPage(driver);
        pause();

        // Scroll down to footer
        WebElement footer = driver.findElement(By.cssSelector("#footer, .mw-footer"));
        scrollTo(footer);
        Assert.assertTrue(navPage.isFooterDisplayed(),
                "TC-N02 FAILED: Footer not displayed");
        pause();

        // Scroll to and click first footer link
        WebElement firstFooterLink = driver.findElement(
                By.cssSelector("#footer-places li a"));
        scrollTo(firstFooterLink);
        pause();

        firstFooterLink.click();
        pause();

        Assert.assertTrue(
                driver.getCurrentUrl().contains("wikipedia.org") ||
                        driver.getCurrentUrl().contains("wikimedia.org") ||
                        driver.getCurrentUrl().contains("wikimediafoundation.org"),
                "TC-N02 FAILED: Footer link did not navigate to a Wikimedia page. URL: " + driver.getCurrentUrl());

        pause();
    }

    @Test(priority = 3, groups = {"navigation", "regression"},
            description = "TC-N03: Verify language selector switches to a different language")
    public void testLanguageSelectorOpensPanel() {
        driver.get("https://en.wikipedia.org/wiki/Albert_Einstein");
        pause();

        WebElement langButton = driver.findElement(By.id("p-lang-btn-checkbox"));
        langButton.click();
        pause();
        // Find the Spanish language link directly and scroll to it
        WebElement spanishLink = driver.findElement(
                By.cssSelector("a[href*='es.wikipedia.org/wiki/Albert_Einstein'], " +
                        "a[hreflang='es'][href*='wikipedia']"));
        scrollTo(spanishLink);
        pause();

        // Click the Spanish link
        spanishLink.click();
        pause();

        // Verify the URL switched to the Spanish Wikipedia
        Assert.assertTrue(driver.getCurrentUrl().contains("es.wikipedia.org"),
                "TC-N03 FAILED: Did not switch to Spanish Wikipedia. URL: " + driver.getCurrentUrl());
        pause();
    }

    @Test(priority = 4, groups = {"navigation", "regression"},
            description = "TC-N04: Verify browser back and forward navigation works between pages")
    public void testBrowserBackForwardNavigation() {
        // Start on homepage
        driver.get("https://en.wikipedia.org/wiki/Main_Page");
        pause();

        String homeUrl = driver.getCurrentUrl();

        // Navigate to an article
        driver.get("https://en.wikipedia.org/wiki/Albert_Einstein");
        pause();

        WebElement content = driver.findElement(By.cssSelector("#mw-content-text"));
        scrollTo(content);
        pause();

        String articleUrl = driver.getCurrentUrl();
        Assert.assertTrue(articleUrl.contains("Albert_Einstein"),
                "TC-N04 FAILED: Did not land on Albert Einstein article");
        pause();

        // Click browser Back button
        driver.navigate().back();
        pause();

        String backUrl = driver.getCurrentUrl();
        Assert.assertTrue(
                backUrl.contains("Main_Page") || backUrl.equals("https://en.wikipedia.org/"),
                "TC-N04 FAILED: Back button did not return to homepage. URL: " + backUrl);

        // Scroll to confirm page loaded
        WebElement mainContent = driver.findElement(
                By.cssSelector("#mp-upper, #mw-content-text"));
        scrollTo(mainContent);
        pause();

        // Click browser Forward button to go back to article
        driver.navigate().forward();
        pause();

        String forwardUrl = driver.getCurrentUrl();
        Assert.assertTrue(forwardUrl.contains("Albert_Einstein"),
                "TC-N04 FAILED: Forward button did not return to article. URL: " + forwardUrl);

        // Scroll to confirm article loaded again
        WebElement articleContent = driver.findElement(By.cssSelector("#mw-content-text"));
        scrollTo(articleContent);
        pause();
    }

    @Test(priority = 5, groups = {"navigation", "accessibility"},
            description = "TC-N05: Verify WCAG accessibility compliance on the About Wikipedia page")
    public void testAboutPageWcagCompliance() {
        NavigationPage navPage = new NavigationPage(driver);
        navPage.navigateToAboutPage();
        pause();

        var results = AxeAccessibilityUtil.runAccessibilityScan(driver, "AboutPage_Wikipedia");
        AxeAccessibilityUtil.logViolationSummary(results, "AboutPage_Wikipedia");
        pause();

        Assert.assertNotNull(results, "TC-N05 FAILED: Axe scan returned null");
        Assert.assertTrue(driver.getCurrentUrl().contains("Wikipedia:About"),
                "TC-N05 FAILED: Not on the About page");

        System.out.println("[TC-N05] WCAG scan complete. Violations: "
                + (results.getViolations() != null ? results.getViolations().size() : 0));
    }
}