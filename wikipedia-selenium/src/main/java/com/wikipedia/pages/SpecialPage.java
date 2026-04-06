package com.wikipedia.pages;

import com.wikipedia.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class SpecialPage extends BasePage {

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
