package com.wikipedia.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import com.wikipedia.utils.Screenshots;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public abstract class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Path downloadDir;

    @BeforeMethod(alwaysRun = true)
    public void setUp() throws IOException {
        System.out.println("========== Starting Test Setup ==========");
        System.out.println("Initializing test environment...");
        downloadDir = Files.createTempDirectory("wikipedia-downloads-");

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", downloadDir.toAbsolutePath().toString());
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.directory_upgrade", true);
        prefs.put("safebrowsing.enabled", true);

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);

        boolean isHeadless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        boolean isCI = System.getenv("CI") != null;
        if (isHeadless || isCI) {
            options.addArguments("--headless=new");
        }

        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();

        System.out.println("Setup completed successfully.");
        System.out.println("========================================\n");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) throws IOException {
        System.out.println("\n========== Starting Test Teardown ==========");
        System.out.println("Cleaning up test environment...");

        if (ITestResult.FAILURE == result.getStatus() && driver != null) {
            Screenshots.takeScreenshot(driver, result.getName());
        }

        if (driver != null) {
            driver.quit();
        }

        if (downloadDir != null && Files.exists(downloadDir)) {
            try (Stream<Path> paths = Files.walk(downloadDir)) {
                paths.sorted((a, b) -> b.compareTo(a)).forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException ignored) {
                        // Best effort cleanup for temp test artifacts.
                    }
                });
            }
        }

        driver = null;
        wait = null;
        downloadDir = null;

        System.out.println("Teardown completed successfully.");
        System.out.println("==========================================\n");
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
