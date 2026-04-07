package com.wikipedia.tests;

import com.wikipedia.pages.ArticlePage;
import com.wikipedia.pages.CategoryPage;
import com.wikipedia.utils.AxeAccessibilityUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CategoryModuleTest extends BaseTest {

    @Test(priority = 1, groups = {"category", "smoke"},
            description = "TC-C01: Verify category page loads and URL contains Category namespace")
    public void testCategoryPageLoadsWithCorrectUrl() throws InterruptedException {
        CategoryPage categoryPage = new CategoryPage(driver);
        categoryPage.openCategory("Mammals");
        pause();

        Assert.assertTrue(driver.getCurrentUrl().contains("Category:"),
                "TC-C01 FAILED: URL does not contain 'Category:'");

        // Scroll to articles section and verify
        WebElement articlesSection = driver.findElement(By.cssSelector("#mw-pages"));
        scrollTo(articlesSection);
        Assert.assertTrue(categoryPage.isArticlesSectionPresent(),
                "TC-C01 FAILED: Articles section not present");
        pause();

        Assert.assertTrue(categoryPage.getArticleLinkCount() > 0,
                "TC-C01 FAILED: No article links found");
        pause();
    }

    @Test(priority = 2, groups = {"category", "regression"},
            description = "TC-C02: Verify articles section is present with links")
    public void testCategoryArticlesSectionHasLinks() throws InterruptedException {
        CategoryPage categoryPage = new CategoryPage(driver);
        categoryPage.openCategory("Physics");
        pause();

        // Scroll to articles section
        WebElement articlesSection = driver.findElement(By.cssSelector("#mw-pages"));
        scrollTo(articlesSection);
        Assert.assertTrue(categoryPage.isArticlesSectionPresent(),
                "TC-C02 FAILED: Articles section not present");
        pause();

        // Scroll to first article link and verify count
        WebElement firstLink = driver.findElement(By.cssSelector("#mw-pages li a"));
        scrollTo(firstLink);
        int linkCount = categoryPage.getArticleLinkCount();
        Assert.assertTrue(linkCount > 0,
                "TC-C02 FAILED: No article links found. Count: " + linkCount);
        pause();
    }

    @Test(priority = 3, groups = {"category", "regression"},
            description = "TC-C03: Verify subcategories section is present on a parent category")
    public void testCategorySubcategoriesSectionIsPresent() throws InterruptedException {
        CategoryPage categoryPage = new CategoryPage(driver);
        categoryPage.openCategory("Science");
        pause();

        // Scroll to subcategories section
        WebElement subSection = driver.findElement(By.cssSelector("#mw-subcategories"));
        scrollTo(subSection);
        Assert.assertTrue(categoryPage.isSubcategoriesSectionPresent(),
                "TC-C03 FAILED: Subcategories section not present");
        pause();

        // Scroll to first subcategory link
        WebElement firstSub = driver.findElement(By.cssSelector("#mw-subcategories li a"));
        scrollTo(firstSub);
        int subCount = categoryPage.getSubcategoryCount();
        Assert.assertTrue(subCount > 0,
                "TC-C03 FAILED: No subcategory links found. Count: " + subCount);
        pause();
    }

    @Test(priority = 4, groups = {"category", "regression"},
            description = "TC-C04: Verify clicking a category article link opens the article page")
    public void testClickingCategoryArticleOpensArticle() throws InterruptedException {
        CategoryPage categoryPage = new CategoryPage(driver);
        categoryPage.openCategory("Countries");
        pause();

        Assert.assertTrue(categoryPage.getArticleLinkCount() > 0,
                "TC-C04 FAILED: No articles found in Countries category");

        // Scroll to first article link then click it
        WebElement firstLink = driver.findElement(By.cssSelector("#mw-pages li a"));
        scrollTo(firstLink);
        pause();

        firstLink.click();
        pause();

        // Scroll to content on the article page
        WebElement content = driver.findElement(By.cssSelector("#mw-content-text"));
        scrollTo(content);
        Assert.assertTrue(driver.getCurrentUrl().contains("wikipedia.org/wiki/"),
                "TC-C04 FAILED: URL does not point to a Wikipedia article");
        pause();
    }

    @Test(priority = 5, groups = {"category", "accessibility"},
            description = "TC-C05: Verify WCAG accessibility compliance on a category page")
    public void testCategoryPageWcagCompliance() throws InterruptedException {
        CategoryPage categoryPage = new CategoryPage(driver);
        categoryPage.openCategory("Technology");
        pause();

        var results = AxeAccessibilityUtil.runAccessibilityScan(driver, "CategoryPage_Technology");
        AxeAccessibilityUtil.logViolationSummary(results, "CategoryPage_Technology");
        pause();

        Assert.assertNotNull(results, "TC-C05 FAILED: Axe scan returned null");
        Assert.assertTrue(driver.getCurrentUrl().contains("Category:"),
                "TC-C05 FAILED: Not on a Category page");

        System.out.println("[TC-C05] WCAG scan complete. Violations: "
                + (results.getViolations() != null ? results.getViolations().size() : 0));
    }
}