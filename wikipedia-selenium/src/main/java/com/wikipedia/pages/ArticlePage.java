package com.wikipedia.pages;

import com.wikipedia.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class ArticlePage extends BasePage {

    public static final String BASE_URL = "https://en.wikipedia.org/wiki/";

    @FindBy(css = "#firstHeading, .firstHeading")
    private WebElement articleTitle;

    @FindBy(css = "#mw-content-text .mw-parser-output")
    private WebElement articleContent;

    @FindBy(css = "#toc, .toc, nav[id*='toc']")
    private WebElement tableOfContents;

    @FindBy(css = "#toc li, .toc li, nav[id*='toc'] li")
    private List<WebElement> tocItems;

    @FindBy(css = ".infobox, .infobox-3p2-left, table.infobox")
    private WebElement infobox;

    @FindBy(css = ".reflist, .references, ol.references")
    private WebElement references;

    @FindBy(css = "a[href^='/wiki/']:not([href*=':']):not([href*='Special'])")
    private List<WebElement> internalLinks;

    @FindBy(css = ".mw-editsection a, a[href*='action=edit']")
    private List<WebElement> editSectionLinks;

    @FindBy(css = "figure img, .image img, a.image img")
    private List<WebElement> articleImages;

    @FindBy(css = "#catlinks, .catlinks")
    private WebElement categoriesSection;

    @FindBy(css = ".mw-indicators, .mw-indicator")
    private List<WebElement> pageIndicators;

    @FindBy(css = "#p-coll-print_export a[href*='printable=yes'], .mw-portlet-coll-print_export a")
    private WebElement printExportLink;

    @FindBy(css = "a[href*='action=history']")
    private WebElement historyLink;

    public ArticlePage(WebDriver driver) {
        super(driver);
    }

    public void openArticle(String articleName) {
        navigateTo(BASE_URL + articleName);
        WaitUtil.waitForVisible(driver, By.cssSelector("#firstHeading, .firstHeading"));
    }

    public String getArticleTitle() {
        WaitUtil.waitForVisible(driver, By.cssSelector("#firstHeading, .firstHeading"));
        return articleTitle.getText();
    }

    public boolean isTableOfContentsPresent() {
        return isElementDisplayed(By.cssSelector("#toc, .toc, #vector-toc, nav[id*='toc']"));
    }

    public boolean isInfoboxPresent() {
        return isElementDisplayed(By.cssSelector(".infobox, table.infobox, .infobox-3p2-left"));
    }

    public boolean isReferenceSectionPresent() {
        return isElementDisplayed(By.cssSelector(".reflist, .references, ol.references"));
    }

    public boolean isCategoryPresent() {
        return isElementDisplayed(By.cssSelector("#catlinks, .catlinks"));
    }

    public List<WebElement> getInternalLinks() {
        return internalLinks;
    }

    public int getImageCount() {
        return driver.findElements(By.cssSelector("figure img, .image img")).size();
    }

    public ArticlePage clickFirstInternalLink() {
        List<WebElement> links = driver.findElements(
                By.cssSelector("#mw-content-text a[href^='/wiki/']:not([href*=':']):not([href*='Special'])"));
        if (!links.isEmpty()) {
            links.get(0).click();
        }
        return new ArticlePage(driver);
    }

    public HistoryPage clickHistoryTab() {
        WaitUtil.waitForClickable(driver, By.cssSelector("a[href*='action=history']")).click();
        return new HistoryPage(driver);
    }

    public boolean isArticleContentPresent() {
        return isElementDisplayed(By.cssSelector("#mw-content-text, .mw-parser-output"));
    }

    public boolean hasEditSectionLinks() {
        return !driver.findElements(By.cssSelector(".mw-editsection")).isEmpty();
    }

    public int getTocItemCount() {
        return driver.findElements(By.cssSelector("#toc li, .toc li, nav[id*='toc'] li")).size();
    }
}
