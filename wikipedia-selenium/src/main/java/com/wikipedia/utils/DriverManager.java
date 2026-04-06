package com.wikipedia.utils;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
public class DriverManager {
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private DriverManager() {}
    public static WebDriver getDriver() {
        if (driverThreadLocal.get() == null) {
            initDriver();
        }
        return driverThreadLocal.get();
    }
    public static void initDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        boolean isHeadless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        boolean isCI = System.getenv("CI") != null;
        if (isHeadless || isCI) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-extensions");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--lang=en-US");
        if (isCI) {
            options.addArguments("--single-process");
            options.addArguments("--no-zygote");
            options.addArguments("--disable-setuid-sandbox");
            options.addArguments("--disable-features=VizDisplayCompositor");
        }
        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driverThreadLocal.set(driver);
    }
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
        }
    }
}
