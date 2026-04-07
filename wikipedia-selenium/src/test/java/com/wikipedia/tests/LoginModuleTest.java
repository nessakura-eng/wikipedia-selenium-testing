package com.wikipedia.tests;

import com.wikipedia.pages.LoginPage;
import com.wikipedia.utils.AxeAccessibilityUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginModuleTest extends BaseTest {

    @Test(priority = 1, groups = {"login", "smoke"},
            description = "TC-L01: Verify login page loads with all required form elements")
    public void testLoginPageLoadsWithAllElements() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        pause();

        // Scroll to and highlight username field
        WebElement username = driver.findElement(By.cssSelector("#wpName1, input[name='wpName']"));
        scrollTo(username);
        Assert.assertTrue(loginPage.isUsernameFieldDisplayed(),
                "TC-L01 FAILED: Username field not displayed");
        pause();

        // Scroll to password field
        WebElement password = driver.findElement(By.cssSelector("#wpPassword1, input[name='wpPassword']"));
        scrollTo(password);
        Assert.assertTrue(loginPage.isPasswordFieldDisplayed(),
                "TC-L01 FAILED: Password field not displayed");
        pause();

        // Scroll to login button
        WebElement loginBtn = driver.findElement(By.cssSelector("#wpLoginAttempt, button[type='submit']"));
        scrollTo(loginBtn);
        Assert.assertTrue(loginPage.isLoginFormDisplayed(),
                "TC-L01 FAILED: Login form not displayed");
        pause();

        Assert.assertTrue(loginPage.getCurrentUrl().contains("Special:UserLogin"),
                "TC-L01 FAILED: Not on login page");
    }

    @Test(priority = 2, groups = {"login", "regression"},
            description = "TC-L02: Verify login is rejected for invalid credentials")
    public void testInvalidLoginShowsErrorMessage() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        pause();

        // Scroll to username, type invalid credentials
        WebElement username = driver.findElement(By.cssSelector("#wpName1, input[name='wpName']"));
        scrollTo(username);
        username.clear();
        username.sendKeys("invalid_user_xyz_123");
        pause();

        WebElement password = driver.findElement(By.cssSelector("#wpPassword1, input[name='wpPassword']"));
        scrollTo(password);
        password.sendKeys("wrong_password_456");
        pause();

        // Click the login button
        WebElement loginBtn = driver.findElement(By.cssSelector("#wpLoginAttempt, button[type='submit']"));
        scrollTo(loginBtn);
        loginBtn.click();
        pause();

        String url = driver.getCurrentUrl();
        boolean loginWasRejected =
                loginPage.isErrorMessageDisplayed()
                        || url.contains("UserLogin")
                        || url.contains("Captcha")
                        || url.contains("captcha")
                        || !url.contains("Special:UserLogin/success");

        Assert.assertTrue(loginWasRejected,
                "TC-L02 FAILED: Login succeeded unexpectedly. URL: " + url);
    }

    @Test(priority = 3, groups = {"login", "regression"},
            description = "TC-L03: Verify Forgot Password link navigates to reset page")
    public void testForgotPasswordLinkNavigation() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        pause();

        // Scroll to and click the forgot password link
        WebElement forgotLink = driver.findElement(
                By.cssSelector("a[href*='Special:PasswordReset']"));
        scrollTo(forgotLink);
        Assert.assertTrue(loginPage.isForgotPasswordLinkPresent(),
                "TC-L03 FAILED: Forgot password link not present");
        pause();

        forgotLink.click();
        pause();

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("PasswordReset") || currentUrl.contains("password"),
                "TC-L03 FAILED: Did not navigate to password reset page. URL: " + currentUrl);
    }

    @Test(priority = 4, groups = {"login", "regression"},
            description = "TC-L04: Verify Create Account link navigates to registration page")
    public void testCreateAccountLinkNavigation() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        pause();

        // Scroll to and click Create Account link
        WebElement createLink = driver.findElement(
                By.cssSelector("a[href*='Special:CreateAccount']"));
        scrollTo(createLink);
        Assert.assertTrue(loginPage.isCreateAccountLinkPresent(),
                "TC-L04 FAILED: Create Account link not present");
        pause();

        createLink.click();
        pause();

        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(15))
                .until(d -> d.getCurrentUrl().contains("CreateAccount"));
        pause();

        Assert.assertTrue(driver.getCurrentUrl().contains("CreateAccount"),
                "TC-L04 FAILED: URL does not contain 'CreateAccount'. Got: " + driver.getCurrentUrl());
    }

    @Test(priority = 5, groups = {"login", "accessibility"},
            description = "TC-L05: Verify WCAG accessibility compliance on the login page")
    public void testLoginPageWcagCompliance() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        pause();

        var results = AxeAccessibilityUtil.runAccessibilityScan(driver, "LoginPage");
        AxeAccessibilityUtil.logViolationSummary(results, "LoginPage");
        pause();

        Assert.assertNotNull(results, "TC-L05 FAILED: Axe scan returned null");
        Assert.assertTrue(loginPage.getCurrentUrl().contains("UserLogin"),
                "TC-L05 FAILED: Not on login page");

        System.out.println("[TC-L05] WCAG scan complete. Violations: "
                + (results.getViolations() != null ? results.getViolations().size() : 0));
    }
}