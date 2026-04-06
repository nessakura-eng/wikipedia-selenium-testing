package com.wikipedia.pages;

import com.wikipedia.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class HistoryPage extends BasePage {

    @FindBy(css = "#pagehistory li, .mw-history-histlinks")
    private List<WebElement> historyEntries;

    @FindBy(css = "#mw-history-compare")
    private WebElement compareButton;

    @FindBy(css = ".mw-changeslist-date, a[href*='oldid=']")
    private List<WebElement> revisionLinks;

    @FindBy(css = ".mw-history-histlinks, #pagehistory")
    private WebElement historyList;

    @FindBy(css = "a[href*='Special:Contributions']")
    private List<WebElement> contributorLinks;

    @FindBy(css = ".mw-pager-navigation-bar, a[title*='older'], a[title*='newer']")
    private WebElement paginationBar;

    public HistoryPage(WebDriver driver) {
        super(driver);
    }

    public void openHistoryFor(String articleName) {
        // Navigate to the article first, then click the History tab
        navigateTo("https://en.wikipedia.org/wiki/" + articleName);
        WaitUtil.waitForVisible(driver, By.cssSelector("#firstHeading, .firstHeading"));
        By historyTabBy = By.cssSelector("a[href*='action=history'], #ca-history a, li#ca-history a");
        WaitUtil.waitForClickable(driver, historyTabBy).click();
        WaitUtil.waitForVisible(driver, By.cssSelector("#pagehistory, .mw-history-histlinks, #mw-content-text"));
    }

    public boolean isHistoryListPresent() {
        return isElementDisplayed(By.cssSelector("#pagehistory, ul#pagehistory"));
    }

    public int getRevisionCount() {
        return driver.findElements(By.cssSelector("#pagehistory li")).size();
    }

    public boolean areRevisionDatesPresent() {
        return !driver.findElements(By.cssSelector("a[href*='oldid=']")).isEmpty();
    }

    public boolean areContributorLinksPresent() {
        return !driver.findElements(By.cssSelector("a[href*='Special:Contributions'], a[href*='User:']")).isEmpty();
    }

    public boolean isCompareButtonPresent() {
        return isElementDisplayed(By.cssSelector("#mw-history-compare, input[type='submit'][name='diff']"));
    }

    public boolean isPaginationPresent() {
        return isElementDisplayed(By.cssSelector(".mw-pager-navigation-bar, a[title*='older revisions']"));
    }
}
