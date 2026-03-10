package pages;

import org.openqa.selenium.By;

public class HomePage extends BasePage {

    // Locators

    By searchBox = By.id("searchInput");
    By searchButton = By.cssSelector("button[type='submit']");
    By englishLanguageLink = By.id("js-link-box-en");
    By donateLink = By.linkText("Donate");
    By footer = By.className("footer");

    // Open homepage

    public void open() {

        driver.get("https://www.wikipedia.org/");

    }

    // Enter text into search box

    public void enterSearch(String text) {

        driver.findElement(searchBox).sendKeys(text);

    }

    // Click search button

    public void clickSearch() {

        driver.findElement(searchButton).click();

    }

    // Full search action

    public void search(String text) {

        enterSearch(text);
        clickSearch();

    }

    // Click English language

    public void openEnglishSite() {

        driver.findElement(englishLanguageLink).click();

    }

    // Verify search box exists

    public boolean isSearchBoxDisplayed() {

        return driver.findElement(searchBox).isDisplayed();

    }

    // Verify Donate link exists

    public boolean isDonateLinkDisplayed() {

        return driver.findElement(donateLink).isDisplayed();

    }

    // Verify footer exists

    public boolean isFooterDisplayed() {

        return driver.findElement(footer).isDisplayed();

    }

}
