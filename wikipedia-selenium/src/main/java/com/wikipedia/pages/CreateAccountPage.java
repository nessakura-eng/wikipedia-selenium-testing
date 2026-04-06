package com.wikipedia.pages;

import com.wikipedia.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CreateAccountPage extends BasePage {

    public static final String URL = "https://en.wikipedia.org/wiki/Special:CreateAccount";

    @FindBy(id = "wpName2")
    private WebElement usernameInput;

    @FindBy(id = "wpPassword2")
    private WebElement passwordInput;

    @FindBy(id = "wpRetype")
    private WebElement retypePasswordInput;

    @FindBy(id = "wpEmail")
    private WebElement emailInput;

    @FindBy(id = "wpCreateaccount")
    private WebElement createAccountButton;

    @FindBy(css = ".mw-ui-form, #userloginForm, form[name='createaccount']")
    private WebElement createAccountForm;

    @FindBy(css = ".errorbox, .mw-message-box-error")
    private WebElement errorMessage;

    @FindBy(css = "a[href*='Special:UserLogin']")
    private WebElement loginLink;

    public CreateAccountPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        navigateTo(URL);
        WaitUtil.waitForVisible(driver, By.cssSelector(
                ".mw-ui-form, form[name='createaccount'], #userloginForm, " +
                ".cdx-text-input, #wpName2, #ooui-php-1"));
    }

    public boolean isCreateAccountFormDisplayed() {
        // Accept either the legacy form, the OOUI form, or the Codex input components
        return isElementDisplayed(By.cssSelector(
                ".mw-ui-form, form[name='createaccount'], #userloginForm")) ||
               isElementDisplayed(By.cssSelector("#wpName2, #ooui-php-1, .cdx-text-input"));
    }

    public boolean isUsernameFieldPresent() {
        return isElementDisplayed(By.id("wpName2"));
    }

    public boolean isPasswordFieldPresent() {
        return isElementDisplayed(By.id("wpPassword2"));
    }

    public boolean isRetypePasswordFieldPresent() {
        return isElementDisplayed(By.id("wpRetype"));
    }

    public boolean isEmailFieldPresent() {
        return isElementDisplayed(By.id("wpEmail"));
    }

    public boolean isCreateAccountButtonPresent() {
        return isElementDisplayed(By.id("wpCreateaccount"));
    }

    public void attemptCreateAccount(String username, String password, String retype, String email) {
        if (isElementDisplayed(By.id("wpName2"))) usernameInput.sendKeys(username);
        if (isElementDisplayed(By.id("wpPassword2"))) passwordInput.sendKeys(password);
        if (isElementDisplayed(By.id("wpRetype"))) retypePasswordInput.sendKeys(retype);
        if (isElementDisplayed(By.id("wpEmail"))) emailInput.sendKeys(email);
        createAccountButton.click();
    }

    public boolean isErrorDisplayed() {
        return isElementDisplayed(By.cssSelector(".errorbox, .mw-message-box-error"));
    }
}
