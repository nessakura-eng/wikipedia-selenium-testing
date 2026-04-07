package com.wikipedia.tests;

import com.wikipedia.utils.DriverManager;
import com.wikipedia.utils.Screenshots;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.ITestResult;

public abstract class BaseTest {

    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverManager.initDriver();
        driver = DriverManager.getDriver();
    }

    @AfterMethod
    public void tearDown(ITestResult result) {

        if (ITestResult.FAILURE == result.getStatus()) {
            Screenshots.takeScreenshot(
                    DriverManager.getDriver(),
                    result.getName()
            );
        }

        DriverManager.quitDriver();
    }

    protected void pause() {
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
    }

    protected void scrollTo(WebElement element) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
        pause();
    }

    protected void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        pause();
    }
}