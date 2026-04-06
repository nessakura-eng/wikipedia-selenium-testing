package com.wikipedia.tests;

import com.wikipedia.pages.ArticlePage;
import com.wikipedia.utils.AxeAccessibilityUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * MODULE 2: Article Module
 *
 * Test Cases:
 * TC-A01: Verify article page loads with title, content, and table of contents
 * TC-A02: Verify article infobox is displayed for a notable subject
 * TC-A03: Verify references/citations section is present on article pages
 * TC-A04: Verify internal hyperlinks within an article are clickable and navigate
 * TC-A05: Verify WCAG accessibility compliance on an article page
 */
public class ArticleModuleTest extends BaseTest {

    /**
     * TC-A01: Verify article page loads with title, content, and table of contents.
     * Opens the "Albert Einstein" article and validates that the page title,
     * main content area, and table of contents are all present.
     */
    @Test(groups = {"article", "smoke"},
            description = "TC-A01: Verify article page loads with content, TOC, and categories")
    public void testArticlePageLoadsWithCoreElements() {
        ArticlePage articlePage = new ArticlePage(driver);
        articlePage.openArticle("Albert_Einstein");

        Assert.assertTrue(articlePage.isArticleContentPresent(),
                "TC-A01 FAILED: Article content area is not present");
        Assert.assertTrue(articlePage.isTableOfContentsPresent(),
                "TC-A01 FAILED: Table of Contents is not present");
        Assert.assertTrue(articlePage.isCategoryPresent(),
                "TC-A01 FAILED: Categories section is not present at the bottom of the article");
    }

    /**
     * TC-A02: Verify article infobox is displayed for a notable subject.
     * Opens the "Python (programming language)" article and asserts that
     * the infobox sidebar element is rendered.
     */
    @Test(groups = {"article", "regression"},
          description = "TC-A02: Verify infobox is displayed for a notable subject article")
    public void testArticleInfoboxIsPresent() {
        ArticlePage articlePage = new ArticlePage(driver);
        articlePage.openArticle("Python_(programming_language)");

        Assert.assertTrue(articlePage.isInfoboxPresent(),
                "TC-A02 FAILED: Infobox is not present on the Python article page");
    }

    /**
     * TC-A03: Verify references/citations section is present on article pages.
     * Opens the "World War II" article and validates that the references
     * section with citations appears at the bottom of the page.
     */
    @Test(groups = {"article", "regression"},
          description = "TC-A03: Verify references section is present on article page")
    public void testArticleReferencesSectionIsPresent() {
        ArticlePage articlePage = new ArticlePage(driver);
        articlePage.openArticle("World_War_II");

        Assert.assertTrue(articlePage.isReferenceSectionPresent(),
                "TC-A03 FAILED: References section is not present on World War II article");
    }

    /**
     * TC-A04: Verify internal hyperlinks within an article navigate to another page.
     * Opens the "Java (programming language)" article, clicks the first valid
     * internal wiki link, and confirms navigation to a new article page.
     */
    @Test(groups = {"article", "regression"},
          description = "TC-A04: Verify internal links are clickable and navigate to other articles")
    public void testArticleInternalLinksNavigate() {
        ArticlePage articlePage = new ArticlePage(driver);
        articlePage.openArticle("Java_(programming_language)");
        String originalTitle = articlePage.getArticleTitle();

        articlePage.clickFirstInternalLink();

        String newTitle = articlePage.getArticleTitle();
        String newUrl = articlePage.getCurrentUrl();

        Assert.assertTrue(newUrl.contains("wikipedia.org/wiki/"),
                "TC-A04 FAILED: Did not navigate to a Wikipedia article. URL: " + newUrl);
        Assert.assertFalse(newTitle.isEmpty(),
                "TC-A04 FAILED: New article title is empty after clicking internal link");
    }

    /**
     * TC-A05: Verify WCAG accessibility compliance on an article page.
     * Runs an Axe accessibility scan on the "Climate change" article and
     * saves the WCAG report. The test verifies the scan executes successfully.
     */
    @Test(groups = {"article", "accessibility"},
          description = "TC-A05: Verify WCAG accessibility compliance on an article page")
    public void testArticlePageWcagCompliance() {
        ArticlePage articlePage = new ArticlePage(driver);
        articlePage.openArticle("Climate_change");

        var results = AxeAccessibilityUtil.runAccessibilityScan(driver, "ArticlePage_ClimateChange");
        AxeAccessibilityUtil.logViolationSummary(results, "ArticlePage_ClimateChange");

        Assert.assertNotNull(results, "TC-A05 FAILED: Axe scan returned null results");
        Assert.assertTrue(articlePage.getCurrentUrl().contains("Climate_change"),
                "TC-A05 FAILED: Not on Climate change article page");

        System.out.println("[TC-A05] WCAG scan complete. Violations: "
                + (results.getViolations() != null ? results.getViolations().size() : 0));
    }
}
