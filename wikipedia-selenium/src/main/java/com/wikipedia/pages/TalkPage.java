package com.wikipedia.pages;

import com.wikipedia.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class TalkPage extends BasePage {

    @FindBy(css = "#firstHeading, .firstHeading")
    private WebElement pageHeading;

    @FindBy(css = ".mw-parser-output h2, #mw-content-text h2")
    private List<WebElement> discussionSections;

    @FindBy(css = "a[href*='action=edit'], .mw-editsection a")
    private List<WebElement> editLinks;

    @FindBy(css = "#toc, .toc")
    private WebElement toc;

    @FindBy(css = ".mw-parser-output")
    private WebElement talkContent;

    public TalkPage(WebDriver driver) {
        super(driver);
    }

    public void openTalkPageFor(String articleName) {
        navigateTo("https://en.wikipedia.org/wiki/Talk:" + articleName);
        WaitUtil.waitForVisible(driver, By.cssSelector("#firstHeading, .firstHeading"));
    }

    public String getHeading() {
        return driver.findElement(By.cssSelector("#firstHeading, .firstHeading")).getText();
    }

    public boolean isTalkPageLoaded() {
        String url = driver.getCurrentUrl();
        return url.contains("Talk:") || url.contains("talk=");
    }

    public boolean hasDiscussionSections() {
        return !driver.findElements(By.cssSelector(".mw-parser-output h2")).isEmpty();
    }

    public int getDiscussionSectionCount() {
        return driver.findElements(By.cssSelector(".mw-parser-output h2")).size();
    }

    public boolean isContentPresent() {
        return isElementDisplayed(By.cssSelector(".mw-parser-output, #mw-content-text"));
    }

    public boolean hasToc() {
        return isElementDisplayed(By.cssSelector("#toc, .toc"));
    }
}
