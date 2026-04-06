package com.wikipedia.pages;

import com.wikipedia.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

    public static final String URL = "https://en.wikipedia.org/wiki/Special:UserLogin";

    @FindBy(id = "wpName1")
    private WebElement usernameInput;

    @FindBy(id = "wpPassword1")
    private WebElement passwordInput;

    @FindBy(id = "wpLoginAttempt")
    private WebElement loginButton;

    @FindBy(css = ".mw-ui-button[href*='Special:CreateAccount'], a[href*='Special:CreateAccount']")
    private WebElement createAccountLink;

    @FindBy(css = "#mw-createaccount-join, a[href*='Special:CreateAccount']")
    private WebElement joinWikipediaLink;

    @FindBy(css = ".errorbox, .mw-message-box-error, #mw-returnto")
    private WebElement loginMessage;

    @FindBy(css = ".errorbox, .mw-message-box-error")
    private WebElement errorMessage;

    @FindBy(css = "a[href*='Special:PasswordReset']")
    private WebElement forgotPasswordLink;

    @FindBy(css = "#wpRemember, input[name='wpRemember']")
    private WebElement rememberMeCheckbox;

    @FindBy(css = ".mw-ui-form, #userloginForm")
    private WebElement loginForm;

    @FindBy(css = "input[name='wpLoginToken'], input[name='token']")
    private WebElement loginToken;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        navigateTo(URL);
        WaitUtil.waitForVisible(driver, By.id("wpName1"));
    }

    public void enterUsername(String username) {
        usernameInput.clear();
        usernameInput.sendKeys(username);
    }

    public void enterPassword(String password) {
        passwordInput.clear();
        passwordInput.sendKeys(password);
    }

    public void clickLoginButton() {
        loginButton.click();
        // Wait briefly for page response (error message or redirect)
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
    }

    public void loginAs(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    public boolean isLoginFormDisplayed() {
        return isElementDisplayed(By.cssSelector(".mw-ui-form, #userloginForm"));
    }

    public boolean isUsernameFieldDisplayed() {
        return isElementDisplayed(By.id("wpName1"));
    }

    public boolean isPasswordFieldDisplayed() {
        return isElementDisplayed(By.id("wpPassword1"));
    }

    public boolean isErrorMessageDisplayed() {
        // Wikipedia uses several different error containers across skin versions
        return isElementDisplayed(By.cssSelector(
                ".errorbox, .mw-message-box-error, .cdx-message--error, " +
                ".mw-htmlform-error-list, .error, [class*='error']"));
    }

    public String getErrorMessageText() {
        String[] selectors = {
            ".errorbox", ".mw-message-box-error", ".cdx-message--error",
            ".mw-htmlform-error-list", ".error"
        };
        for (String sel : selectors) {
            try {
                WebElement el = driver.findElement(By.cssSelector(sel));
                if (el.isDisplayed()) return el.getText();
            } catch (Exception ignored) {}
        }
        return "";
    }

    public boolean isCreateAccountLinkPresent() {
        return isElementDisplayed(By.cssSelector("a[href*='Special:CreateAccount']"));
    }

    public boolean isForgotPasswordLinkPresent() {
        return isElementDisplayed(By.cssSelector("a[href*='Special:PasswordReset']"));
    }

    public boolean isRememberMePresent() {
        return isElementDisplayed(By.cssSelector("#wpRemember, input[name='wpRemember']"));
    }

    public CreateAccountPage clickCreateAccount() {
        WaitUtil.waitForClickable(driver, By.cssSelector("a[href*='Special:CreateAccount']")).click();
        return new CreateAccountPage(driver);
    }

    public void clickForgotPassword() {
        WaitUtil.waitForClickable(driver, By.cssSelector("a[href*='Special:PasswordReset']")).click();
    }
}
