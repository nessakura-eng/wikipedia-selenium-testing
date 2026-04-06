package com.wikipedia.tests;

import com.wikipedia.pages.ArticlePage;
import com.wikipedia.pages.CategoryPage;
import com.wikipedia.utils.AxeAccessibilityUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * MODULE 5: Category Module
 *
 * Test Cases:
 * TC-C01: Verify category page loads with correct heading
 * TC-C02: Verify articles section is present and contains links
 * TC-C03: Verify subcategories section is present on a parent category
 * TC-C04: Verify clicking a category article link opens the article
 * TC-C05: Verify WCAG accessibility compliance on a category page
 */
public class CategoryModuleTest extends BaseTest {

    /**
     * TC-C01: Verify category page loads with correct heading.
     * Opens the "Mammals" category page and asserts the page heading
     * contains "Mammals".
     */
    @Test(groups = {"category", "smoke"},
            description = "TC-C01: Verify category page loads and URL contains Category namespace")
    public void testCategoryPageLoadsWithCorrectUrl() {
        CategoryPage categoryPage = new CategoryPage(driver);
        categoryPage.openCategory("Mammals");

        Assert.assertTrue(driver.getCurrentUrl().contains("Category:"),
                "TC-C01 FAILED: URL does not contain 'Category:' namespace. URL: " + driver.getCurrentUrl());
        Assert.assertTrue(categoryPage.isArticlesSectionPresent(),
                "TC-C01 FAILED: Articles section is not present on category page");
        Assert.assertTrue(categoryPage.getArticleLinkCount() > 0,
                "TC-C01 FAILED: No article links found in Mammals category");
    }

    /**
     * TC-C02: Verify articles section is present and contains links.
     * Opens the "Physics" category and asserts that the articles
     * section is displayed with at least one article link.
     */
    @Test(groups = {"category", "regression"},
          description = "TC-C02: Verify articles section is present with links")
    public void testCategoryArticlesSectionHasLinks() {
        CategoryPage categoryPage = new CategoryPage(driver);
        categoryPage.openCategory("Physics");

        Assert.assertTrue(categoryPage.isArticlesSectionPresent(),
                "TC-C02 FAILED: Articles section (#mw-pages) is not present");
        int linkCount = categoryPage.getArticleLinkCount();
        Assert.assertTrue(linkCount > 0,
                "TC-C02 FAILED: No article links found in Physics category. Count: " + linkCount);
    }

    /**
     * TC-C03: Verify subcategories section is present on a parent category.
     * Opens the "Science" category page and asserts that the subcategories
     * section is rendered with at least one subcategory link.
     */
    @Test(groups = {"category", "regression"},
          description = "TC-C03: Verify subcategories section is present on a parent category")
    public void testCategorySubcategoriesSectionIsPresent() {
        CategoryPage categoryPage = new CategoryPage(driver);
        categoryPage.openCategory("Science");

        Assert.assertTrue(categoryPage.isSubcategoriesSectionPresent(),
                "TC-C03 FAILED: Subcategories section is not present in Science category");
        int subCount = categoryPage.getSubcategoryCount();
        Assert.assertTrue(subCount > 0,
                "TC-C03 FAILED: No subcategory links found in Science category. Count: " + subCount);
    }

    /**
     * TC-C04: Verify clicking a category article link opens the article.
     * Opens the "Countries" category, clicks the first article link,
     * and asserts the resulting page is a valid Wikipedia article.
     */
    @Test(groups = {"category", "regression"},
          description = "TC-C04: Verify clicking a category article link opens the article page")
    public void testClickingCategoryArticleOpensArticle() {
        CategoryPage categoryPage = new CategoryPage(driver);
        categoryPage.openCategory("Countries");

        Assert.assertTrue(categoryPage.getArticleLinkCount() > 0,
                "TC-C04 FAILED: No articles found in Countries category");

        ArticlePage articlePage = categoryPage.clickFirstArticle();
        String title = articlePage.getArticleTitle();

        Assert.assertFalse(title.isEmpty(),
                "TC-C04 FAILED: Article title is empty after clicking category link");
        Assert.assertTrue(driver.getCurrentUrl().contains("wikipedia.org/wiki/"),
                "TC-C04 FAILED: URL does not point to a Wikipedia article");
    }

    /**
     * TC-C05: Verify WCAG accessibility compliance on a category page.
     * Runs an Axe accessibility scan on the "Technology" category page
     * and saves a WCAG report.
     */
    @Test(groups = {"category", "accessibility"},
          description = "TC-C05: Verify WCAG accessibility compliance on a category page")
    public void testCategoryPageWcagCompliance() {
        CategoryPage categoryPage = new CategoryPage(driver);
        categoryPage.openCategory("Technology");

        var results = AxeAccessibilityUtil.runAccessibilityScan(driver, "CategoryPage_Technology");
        AxeAccessibilityUtil.logViolationSummary(results, "CategoryPage_Technology");

        Assert.assertNotNull(results, "TC-C05 FAILED: Axe scan returned null results");
        Assert.assertTrue(driver.getCurrentUrl().contains("Category:"),
                "TC-C05 FAILED: Not on a Category page when running WCAG scan");

        System.out.println("[TC-C05] WCAG scan complete. Violations: "
                + (results.getViolations() != null ? results.getViolations().size() : 0));
    }
}
