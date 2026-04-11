package com.wikipedia.pages;

import com.wikipedia.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

public class SpecialPage extends BasePage {

    public static final String MAIN_PAGE_URL = "https://en.wikipedia.org/wiki/Main_Page";
    public static final String SPECIAL_PAGES_URL = "https://en.wikipedia.org/wiki/Special:SpecialPages";
    public static final String RECENT_CHANGES_URL = "https://en.wikipedia.org/wiki/Special:RecentChanges";
    public static final String RANDOM_URL = "https://en.wikipedia.org/wiki/Special:Random";
    public static final String STATISTICS_URL = "https://en.wikipedia.org/wiki/Special:Statistics";
    public static final String WANTED_PAGES_URL = "https://en.wikipedia.org/wiki/Special:WantedPages";
    public static final String NEW_PAGES_URL = "https://en.wikipedia.org/wiki/Special:NewPages";

    @FindBy(css = "#firstHeading, .firstHeading")
    private WebElement pageHeading;

    @FindBy(css = ".mw-specialpages-group, .mw-special-Specialpages")
    private List<WebElement> specialPageGroups;

    @FindBy(css = "ul.mw-specialpages-list li a, .mw-specialpages-group li a")
    private List<WebElement> specialPageLinks;

    @FindBy(css = ".mw-changeslist li, .mw-recentchanges-table")
    private List<WebElement> recentChangeEntries;

    @FindBy(css = ".wikistats td, table.wikitable td")
    private List<WebElement> statisticsCells;

    @FindBy(css = ".mw-rcfilters-ui-filterGroupWidget, .rcselector")
    private WebElement rcFilter;

    public SpecialPage(WebDriver driver) {
        super(driver);
    }

    public void openSpecialPages() {
        navigateTo("https://en.wikipedia.org/wiki/Main_Page");
        WaitUtil.waitForVisible(driver, By.cssSelector("#mp-upper, #mw-content-text"));
        WebElement link = driver.findElement(
                By.cssSelector("a[href*='Special:SpecialPages']"));
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", link);
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", link);
        WaitUtil.waitForVisible(driver, By.cssSelector("#mw-content-text"));
    }

    public void openMainPage() {
        navigateTo(MAIN_PAGE_URL);
        WaitUtil.waitForVisible(driver, By.cssSelector("#mw-content-text, #mp-upper"));
    }

    public void openRecentChanges() {
        navigateTo(SPECIAL_PAGES_URL);
        WaitUtil.waitForVisible(driver, By.cssSelector("#mw-content-text"));
        WebElement link = driver.findElement(
                By.cssSelector("a[href*='Special:RecentChanges']"));
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", link);
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", link);
        WaitUtil.waitForVisible(driver, By.cssSelector(".mw-changeslist, #mw-content-text"));
    }

    public void openStatistics() {
        navigateTo(SPECIAL_PAGES_URL);
        WaitUtil.waitForVisible(driver, By.cssSelector("#mw-content-text"));
        WebElement link = driver.findElement(
                By.cssSelector("a[href*='Special:Statistics']"));
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", link);
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", link);
        WaitUtil.waitForVisible(driver, By.cssSelector("table.wikitable, #mw-content-text table"));
    }

    public void openWantedPages() {
        navigateTo(WANTED_PAGES_URL);
        WaitUtil.waitForVisible(driver, By.cssSelector("#firstHeading, .firstHeading"));
    }

    public void openNewPages() {
        navigateTo(NEW_PAGES_URL);
        WaitUtil.waitForVisible(driver, By.cssSelector("#firstHeading, .firstHeading"));
    }

    public void openToolsMenu() {
        By toolsToggle = By.cssSelector("#vector-page-tools-dropdown-label");
        WebElement tools = WaitUtil.waitForClickable(driver, toolsToggle);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", tools);
        WaitUtil.waitForVisible(driver, By.cssSelector("#p-tb, #vector-page-tools-unpinned-container"));
    }

    public boolean isToolLinkPresent(String toolName) {
        return isElementDisplayed(By.xpath("//a[normalize-space()='" + toolName + "']"));
    }

    public void openCiteThisPageFromTools() {
        openToolsMenu();
        By citeLocator = By.cssSelector("#t-cite a, a[href*='Special:CiteThisPage']");
        List<WebElement> links = driver.findElements(citeLocator);
        if (!links.isEmpty()) {
            WebElement citeLink = links.get(0);
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", citeLink);
        } else {
            navigateTo("https://en.wikipedia.org/w/index.php?title=Special:CiteThisPage&page=Main_Page");
        }
        WaitUtil.waitForUrl(driver, "Special:CiteThisPage");
        WaitUtil.waitForVisible(driver, By.cssSelector("#mw-content-text"));
    }

    public void openGetShortenedUrlFromTools() {
        openToolsMenu();
        By shortUrlLocator = By.cssSelector("#t-urlshortener a, a[href*='Special:UrlShortener']");
        List<WebElement> links = driver.findElements(shortUrlLocator);
        String encoded = URLEncoder.encode(driver.getCurrentUrl(), StandardCharsets.UTF_8);
        String fallbackUrl = "https://en.wikipedia.org/w/index.php?title=Special:UrlShortener&url=" + encoded;

        if (!links.isEmpty()) {
            WebElement shortUrlLink = links.get(0);
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", shortUrlLink);
        } else {
            navigateTo(fallbackUrl);
        }

        if (!driver.getCurrentUrl().contains("Special:UrlShortener")) {
            navigateTo(fallbackUrl);
        }

        WaitUtil.waitForUrl(driver, "Special:UrlShortener");
        WaitUtil.waitForVisible(driver, By.cssSelector("#mw-content-text"));
    }

    public void clickShortenButton() {
        WebElement shortenButton = WaitUtil.waitForClickable(
                driver,
                By.cssSelector("#mw-urlshortener-submit button, button[value='Shorten']"));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", shortenButton);
        new WebDriverWait(driver, Duration.ofSeconds(15)).until(
                ExpectedConditions.or(
                        ExpectedConditions.urlContains("w.wiki"),
                        ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "w.wiki/")
                ));
    }

    public boolean hasGeneratedShortUrl() {
        String source = driver.getPageSource();
        return source.contains("w.wiki/");
    }

    public boolean selectAppearanceOption(String optionLabel) {
        By appearanceToggle = By.cssSelector("#vector-appearance-dropdown-label");
        WebElement appearance = WaitUtil.waitForClickable(driver, appearanceToggle);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", appearance);

        By optionLocator = By.xpath("//label[normalize-space()='" + optionLabel + "']");
        WebElement label = WaitUtil.waitForClickable(driver, optionLocator);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", label);

        String optionId = label.getAttribute("for");
        if (optionId == null || optionId.isBlank()) {
            return false;
        }

        WebElement input = driver.findElement(By.id(optionId));
        return input.isSelected();
    }

    public void unpinAppearancePanel() {
        By hideButton = By.cssSelector("button[data-event-name='pinnable-header.vector-appearance.unpin']");
        WebElement button = WaitUtil.waitForClickable(driver, hideButton);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                d -> d.findElement(By.tagName("html")).getAttribute("class")
                        .contains("vector-feature-appearance-pinned-clientpref-0"));
    }

    public void pinAppearancePanel() {
        By pinButton = By.cssSelector("button[data-event-name='pinnable-header.vector-appearance.pin']");
        WebElement button = WaitUtil.waitForClickable(driver, pinButton);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                d -> d.findElement(By.tagName("html")).getAttribute("class")
                        .contains("vector-feature-appearance-pinned-clientpref-1"));
    }

    public String getHtmlClassList() {
        return driver.findElement(By.tagName("html")).getAttribute("class");
    }

    public boolean isSpecialPagesListPresent() {
        // Vector 2022 uses div.mw-specialpages-group > ul or a flat list under #mw-content-text
        return isElementDisplayed(By.cssSelector(
                "ul.mw-specialpages-list, .mw-specialpages-group, " +
                "#mw-content-text ul, #mw-content-text table"));
    }

    public int getSpecialPageLinkCount() {
        // Count links inside any list on the Special:SpecialPages page
        int count = driver.findElements(By.cssSelector(
                "ul.mw-specialpages-list li a, .mw-specialpages-group li a, " +
                "#mw-content-text li a")).size();
        return count;
    }

    public boolean isRecentChangesListPresent() {
        return isElementDisplayed(By.cssSelector(".mw-changeslist, .mw-recentchanges-table, #mw-content-text ul"));
    }

    public int getRecentChangesCount() {
        return driver.findElements(By.cssSelector(".mw-changeslist-line, .mw-recentchanges-table tr")).size();
    }

    public boolean isStatisticsTablePresent() {
        return isElementDisplayed(By.cssSelector("table.wikitable, .wikistats, #mw-content-text table"));
    }

    public boolean isWantedPagesTablePresent() {
        return isElementDisplayed(By.cssSelector("ol.special, table.wikitable, #mw-content-text ol"));
    }

    public String getPageHeadingText() {
        return driver.findElement(By.cssSelector("#firstHeading, .firstHeading")).getText();
    }

    public boolean isNewPagesListPresent() {
        return isElementDisplayed(By.cssSelector(".mw-newpages-list, ul.special, #mw-content-text ul"));
    }
}
