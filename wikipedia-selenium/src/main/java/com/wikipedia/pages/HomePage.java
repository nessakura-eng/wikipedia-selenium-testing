package com.wikipedia.pages;

import com.wikipedia.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class HomePage extends BasePage {

    public static final String URL = "https://en.wikipedia.org/wiki/Main_Page";

    @FindBy(id = "searchInput")
    private WebElement legacySearchInput;

    @FindBy(css = "input[type='search'], #searchInput, [name='search']")
    private WebElement searchBox;

    @FindBy(id = "searchButton")
    private WebElement searchButton;

    @FindBy(css = ".mp-tfa-h2, #mp-tfa-h2, h2[id*='featured']")
    private WebElement featuredArticleHeading;

    @FindBy(css = "#mp-upper, .mw-parser-output")
    private WebElement mainContent;

    @FindBy(css = "#n-mainpage-description a, a[href='/wiki/Main_Page']")
    private WebElement mainPageLink;

    @FindBy(css = "#mp-tfa, #mp-upper")
    private WebElement featuredArticleSection;

    @FindBy(css = "#mp-itn, .mp-itn")
    private WebElement inTheNewsSection;

    @FindBy(css = "#mp-dyk, .mp-dyk")
    private WebElement didYouKnowSection;

    @FindBy(css = "#mp-otd, .mp-otd")
    private WebElement onThisDaySection;

    @FindBy(css = "#mp-lower")
    private WebElement lowerMainContent;

    @FindBy(css = "a[href*='Special:Random']")
    private WebElement randomArticleLink;

    @FindBy(css = "#p-lang-btn, .mw-interlanguage-selector, [data-event-name='menu.languages']")
    private WebElement languageSelector;

    @FindBy(css = ".mw-logo, #p-logo a, a.mw-wiki-logo")
    private WebElement wikiLogo;

    @FindBy(css = "#p-navigation .vector-menu-content-list li, #mw-panel .portal .body ul li")
    private List<WebElement> navigationLinks;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        navigateTo(URL);
        WaitUtil.waitForTitle(driver, "Wikipedia");
    }

    public ArticlePage search(String query) {
        // Vector 2022: search input lives inside #searchform
        By searchBy = By.cssSelector(
                "#searchform input[type='search'], #searchform input[name='search'], " +
                ".cdx-search-input input, #searchInput, input[name='search']");
        WebElement input = WaitUtil.waitForVisible(driver, searchBy);
        input.clear();
        input.sendKeys(query);
        input.sendKeys(Keys.ENTER);
        return new ArticlePage(driver);
    }

    public boolean isSearchBoxDisplayed() {
        return isElementDisplayed(By.cssSelector(
                "#searchform input[type='search'], #searchform input[name='search'], " +
                ".cdx-search-input input, #searchInput, input[name='search']"));
    }

    public boolean isFeaturedArticleSectionPresent() {
        return isElementDisplayed(By.cssSelector("#mp-tfa, #mp-upper, .mp-tfa"));
    }

    public boolean isInTheNewsSectionPresent() {
        return isElementDisplayed(By.cssSelector("#mp-itn, .mp-itn, #In_the_news"));
    }

    public boolean isDidYouKnowSectionPresent() {
        return isElementDisplayed(By.cssSelector("#mp-dyk, .mp-dyk, #Did_you_know"));
    }

    public boolean isOnThisDaySectionPresent() {
        return isElementDisplayed(By.cssSelector("#mp-otd, .mp-otd, #On_this_day"));
    }

    public ArticlePage clickRandomArticle() throws InterruptedException {
        // The Random Article link exists in the DOM but may be inside a collapsed sidebar.
        // Use JavaScript to click it since waitForClickable requires visibility.
        WebElement menu = driver.findElement(By.id("vector-main-menu-dropdown-checkbox"));
        menu.click();
        Thread.sleep(2000);

        By randomLinkBy = By.cssSelector("#n-randompage a, a[href*='Special:Random']");
        WebElement link = driver.findElement(randomLinkBy);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
        WaitUtil.waitForVisible(driver, By.cssSelector("#firstHeading, .firstHeading"));
        Thread.sleep(2000);
        return new ArticlePage(driver);
    }

    public boolean isRandomArticleLinkPresent() {
        return isElementDisplayed(By.cssSelector("a[href*='Special:Random'], #n-randompage a"));
    }

    public boolean isLogoDisplayed() {
        return isElementDisplayed(By.cssSelector(".mw-logo, #p-logo a, a.mw-wiki-logo"));
    }

    public boolean isNavigationPresent() {
        return isElementDisplayed(By.cssSelector("#vector-main-menu, #mw-panel, .mw-portlet-navigation"));
    }

    public String getMainPageTitle() {
        return driver.getTitle();
    }

    public List<WebElement> getNavigationLinks() {
        return navigationLinks;
    }
}
