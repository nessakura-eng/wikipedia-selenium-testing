package com.wikipedia.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

public class Screenshots {

    public static void takeScreenshot(WebDriver driver, String testName) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            String timestamp = LocalDateTime.now()
                    .toString()
                    .replace(":", "-");

            String path = "target/screenshots/" + testName + "_" + timestamp + ".png";

            Files.createDirectories(Paths.get("target/screenshots"));
            Files.copy(src.toPath(), Paths.get(path), StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Screenshot saved: " + path);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}