package com.wikipedia.pages;

import com.wikipedia.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class NavigationPage extends BasePage {

    @FindBy(css = ".mw-logo, a.mw-wiki-logo, #p-logo a")
    private WebElement wikiLogo;

    @FindBy(css = "#vector-main-menu, #mw-panel, .mw-portlet-navigation")
    private WebElement mainMenu;

    @FindBy(css = "a[href*='Special:Random']")
    private WebElement randomArticleLink;

    @FindBy(css = "a[href*='Wikipedia:Contents']")
    private WebElement contentsLink;

    @FindBy(css = "a[href*='Wikipedia:About']")
    private WebElement aboutLink;

    @FindBy(css = "a[href*='Wikipedia:Contact_us']")
    private WebElement contactLink;

    @FindBy(css = "#footer, .mw-footer")
    private WebElement footer;

    @FindBy(css = "#footer-places li, .footer-places li")
    private List<WebElement> footerLinks;

    @FindBy(css = "#footer-info-lastmod, .footer-info-lastmod")
    private WebElement lastModified;

    @FindBy(css = ".mw-portlet-lang, #p-lang-btn")
    private WebElement languageButton;

    @FindBy(css = "#p-views .mw-list-item a, .vector-tab-noicon a")
    private List<WebElement> pageViewTabs;

    @FindBy(css = "a[href*='action=edit'], #ca-edit a")
    private WebElement editButton;

    @FindBy(css = "a[href*='action=history'], #ca-history a")
    private WebElement historyButton;

    public NavigationPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLogoDisplayed() {
        return isElementDisplayed(By.cssSelector(".mw-logo, a.mw-wiki-logo, #p-logo a"));
    }

    public boolean isMainMenuDisplayed() {
        return isElementDisplayed(By.cssSelector("#vector-main-menu, #mw-panel, .mw-portlet-navigation"));
    }

    public boolean isFooterDisplayed() {
        return isElementDisplayed(By.cssSelector("#footer, .mw-footer"));
    }

    public void clickLogo() {
        WaitUtil.waitForClickable(driver, By.cssSelector(".mw-logo, a.mw-wiki-logo, #p-logo a")).click();
    }

    public void clickRandomArticle() {
        By randomLinkBy = By.cssSelector("#n-randompage a, a[href*='Special:Random']");
        WebElement link = driver.findElement(randomLinkBy);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
        WaitUtil.waitForVisible(driver, By.cssSelector("#firstHeading, .firstHeading"));
    }

    public boolean isRandomArticleLinkPresent() {
        // The link exists in the DOM even if the sidebar is collapsed
        return !driver.findElements(By.cssSelector("a[href*='Special:Random']")).isEmpty();
    }

    public boolean isFooterLastModifiedPresent() {
        return isElementDisplayed(By.cssSelector("#footer-info-lastmod, li#footer-info-lastmod"));
    }

    public List<WebElement> getFooterLinks() {
        return driver.findElements(By.cssSelector("#footer-places li a, .footer-places li a"));
    }

    public int getFooterLinkCount() {
        return driver.findElements(By.cssSelector("#footer-places li a")).size();
    }

    public boolean isLanguageButtonPresent() {
        return isElementDisplayed(By.cssSelector(".mw-portlet-lang, #p-lang-btn"));
    }

    public boolean isEditTabPresent() {
        return isElementDisplayed(By.cssSelector("a[href*='action=edit'], #ca-edit"));
    }

    public boolean isHistoryTabPresent() {
        return isElementDisplayed(By.cssSelector("a[href*='action=history'], #ca-history"));
    }

    public void navigateToAboutPage() {
        navigateTo("https://en.wikipedia.org/wiki/Main_Page");
        WaitUtil.waitForVisible(driver, By.cssSelector("#mp-upper, #mw-content-text"));
        // The About link is in the footer — scroll to it and JS click
        WebElement aboutLink = driver.findElement(
                By.cssSelector("a[href*='Wikipedia:About']"));
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", aboutLink);
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", aboutLink);
        WaitUtil.waitForVisible(driver, By.cssSelector("#firstHeading, .firstHeading"));
    }
}
