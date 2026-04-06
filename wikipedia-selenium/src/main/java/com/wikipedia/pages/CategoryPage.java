package com.wikipedia.pages;

import com.wikipedia.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class CategoryPage extends BasePage {

    public static final String BASE_URL = "https://en.wikipedia.org/wiki/Category:";

    @FindBy(css = "#firstHeading, .firstHeading")
    private WebElement pageHeading;

    @FindBy(css = "#mw-pages .mw-category, #mw-pages")
    private WebElement articlesSection;

    @FindBy(css = "#mw-pages .mw-category-group li a, #mw-pages li a")
    private List<WebElement> articleLinks;

    @FindBy(css = "#mw-subcategories, .mw-category-generated")
    private WebElement subcategoriesSection;

    @FindBy(css = "#mw-subcategories li a")
    private List<WebElement> subcategoryLinks;

    @FindBy(css = "a[href*='pagefrom=']")
    private WebElement nextPageLink;

    @FindBy(css = "#mw-content-text .mw-parser-output p")
    private WebElement categoryDescription;

    public CategoryPage(WebDriver driver) {
        super(driver);
    }

    public void openCategory(String categoryName) {
        // Search for the category, then click the Category link from the article's bottom
        navigateTo("https://en.wikipedia.org/wiki/" + categoryName);
        WaitUtil.waitForVisible(driver, By.cssSelector("#firstHeading, .firstHeading"));
        // Scroll down to the categories section and click the matching category link
        By categoryLinkBy = By.cssSelector(
                "#catlinks a[href*='Category:'], .catlinks a[href*='Category:']");
        java.util.List<org.openqa.selenium.WebElement> catLinks =
                driver.findElements(categoryLinkBy);
        if (!catLinks.isEmpty()) {
            scrollToElement(catLinks.get(0));
            catLinks.get(0).click();
            WaitUtil.waitForVisible(driver, By.cssSelector("#mw-pages, #mw-subcategories, #firstHeading"));
        } else {
            // Fallback: navigate directly if no category link found on the article page
            navigateTo(BASE_URL + categoryName);
            WaitUtil.waitForVisible(driver, By.cssSelector("#firstHeading, .firstHeading"));
        }
    }

    public String getCategoryHeading() {
        return driver.findElement(By.cssSelector("#firstHeading, .firstHeading")).getText();
    }

    public boolean isArticlesSectionPresent() {
        return isElementDisplayed(By.cssSelector("#mw-pages"));
    }

    public int getArticleLinkCount() {
        return driver.findElements(By.cssSelector("#mw-pages li a")).size();
    }

    public boolean isSubcategoriesSectionPresent() {
        return isElementDisplayed(By.cssSelector("#mw-subcategories"));
    }

    public int getSubcategoryCount() {
        return driver.findElements(By.cssSelector("#mw-subcategories li a")).size();
    }

    public boolean isNextPagePresent() {
        return isElementDisplayed(By.cssSelector("a[href*='pagefrom=']"));
    }

    public ArticlePage clickFirstArticle() {
        List<WebElement> links = driver.findElements(By.cssSelector("#mw-pages li a"));
        if (!links.isEmpty()) {
            links.get(0).click();
        }
        return new ArticlePage(driver);
    }

    public boolean isCategoryHeadingContains(String text) {
        String heading = getCategoryHeading();
        return heading.toLowerCase().contains(text.toLowerCase());
    }
}
