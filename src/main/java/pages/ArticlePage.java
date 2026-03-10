package pages;

import org.openqa.selenium.By;

public class ArticlePage extends BasePage {

    // Locators

    By articleTitle = By.id("firstHeading");
    By tableOfContents = By.id("toc");
    By referencesSection = By.id("References");
    By infobox = By.className("infobox");
    By firstParagraph = By.cssSelector("p");

    // Open a specific article

    public void openSeleniumArticle() {

        driver.get("https://en.wikipedia.org/wiki/Selenium_(software)");

    }

    public void openAutomationTestingArticle() {

        driver.get("https://en.wikipedia.org/wiki/Test_automation");

    }

    // Element checks

    public boolean isTitleDisplayed() {

        return driver.findElement(articleTitle).isDisplayed();

    }

    public boolean isTableOfContentsDisplayed() {

        return driver.findElement(tableOfContents).isDisplayed();

    }

    public boolean isReferencesDisplayed() {

        return driver.findElement(referencesSection).isDisplayed();

    }

    public boolean isInfoboxDisplayed() {

        return driver.findElement(infobox).isDisplayed();

    }

    public boolean isFirstParagraphDisplayed() {

        return driver.findElement(firstParagraph).isDisplayed();

    }

}
