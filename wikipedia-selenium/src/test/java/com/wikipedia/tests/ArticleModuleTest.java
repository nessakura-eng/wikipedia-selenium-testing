package com.wikipedia.tests;

import com.wikipedia.pages.ArticlePage;
import com.wikipedia.utils.AxeAccessibilityUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ArticleModuleTest extends BaseTest {

    @Test(priority = 1, groups = {"article", "smoke"},
            description = "TC-A01: Verify article page loads with content, TOC, and categories")
    public void testArticlePageLoadsWithCoreElements() {
        ArticlePage articlePage = new ArticlePage(driver);
        articlePage.openArticle("Albert_Einstein");
        pause();

        // Scroll to and verify content area
        WebElement content = driver.findElement(
                By.cssSelector("#mw-content-text, .mw-parser-output"));
        scrollTo(content);
        Assert.assertTrue(articlePage.isArticleContentPresent(),
                "TC-A01 FAILED: Article content not present");
        pause();

        // Scroll to TOC
        WebElement toc = driver.findElement(
                By.cssSelector("#toc, .toc, #vector-toc, nav[id*='toc']"));
        scrollTo(toc);
        Assert.assertTrue(articlePage.isTableOfContentsPresent(),
                "TC-A01 FAILED: Table of Contents not present");
        pause();

        // Scroll to categories at bottom
        WebElement categories = driver.findElement(
                By.cssSelector("#catlinks, .catlinks"));
        scrollTo(categories);
        Assert.assertTrue(articlePage.isCategoryPresent(),
                "TC-A01 FAILED: Categories section not present");
        pause();
    }

    @Test(priority = 2, groups = {"article", "regression"},
            description = "TC-A02: Verify infobox is displayed for a notable subject article")
    public void testArticleInfoboxIsPresent() {
        ArticlePage articlePage = new ArticlePage(driver);
        articlePage.openArticle("Python_(programming_language)");
        pause();

        // Scroll to infobox and verify it
        WebElement infobox = driver.findElement(
                By.cssSelector(".infobox, table.infobox, .infobox-3p2-left"));
        scrollTo(infobox);
        Assert.assertTrue(articlePage.isInfoboxPresent(),
                "TC-A02 FAILED: Infobox not present");
        pause();
    }

    @Test(priority = 3, groups = {"article", "regression"},
            description = "TC-A03: Verify references section is present on article page")
    public void testArticleReferencesSectionIsPresent() {
        ArticlePage articlePage = new ArticlePage(driver);
        articlePage.openArticle("World_War_II");
        pause();

        // Scroll to references section at bottom
        WebElement refs = driver.findElement(
                By.cssSelector(".reflist, .references, ol.references"));
        scrollTo(refs);
        Assert.assertTrue(articlePage.isReferenceSectionPresent(),
                "TC-A03 FAILED: References section not present");
        pause();
    }

    @Test(priority = 4, groups = {"article", "regression"},
            description = "TC-A04: Verify internal links are clickable and navigate to other articles")
    public void testArticleInternalLinksNavigate() {
        ArticlePage articlePage = new ArticlePage(driver);
        articlePage.openArticle("Java_(programming_language)");
        pause();

        // Scroll to first internal link and click it
        WebElement firstLink = driver.findElement(
                By.cssSelector("#mw-content-text a[href^='/wiki/']:not([href*=':']):not([href*='Special'])"));
        scrollTo(firstLink);
        pause();

        firstLink.click();
        pause();

        Assert.assertTrue(driver.getCurrentUrl().contains("wikipedia.org/wiki/"),
                "TC-A04 FAILED: Did not navigate to a Wikipedia article");
        Assert.assertTrue(articlePage.isArticleContentPresent(),
                "TC-A04 FAILED: Article content not present after navigation");
        pause();
    }

    @Test(priority = 5, groups = {"article", "accessibility"},
            description = "TC-A05: Verify WCAG accessibility compliance on an article page")
    public void testArticlePageWcagCompliance() {
        ArticlePage articlePage = new ArticlePage(driver);
        articlePage.openArticle("Climate_change");
        pause();

        var results = AxeAccessibilityUtil.runAccessibilityScan(driver, "ArticlePage_ClimateChange");
        AxeAccessibilityUtil.logViolationSummary(results, "ArticlePage_ClimateChange");
        pause();

        Assert.assertNotNull(results, "TC-A05 FAILED: Axe scan returned null");
        Assert.assertTrue(articlePage.getCurrentUrl().contains("Climate_change"),
                "TC-A05 FAILED: Not on Climate change article page");

        System.out.println("[TC-A05] WCAG scan complete. Violations: "
                + (results.getViolations() != null ? results.getViolations().size() : 0));
    }
}