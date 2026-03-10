package pages;

import org.openqa.selenium.WebDriver;
import utils.DriverFactory;

public class BasePage {

    protected WebDriver driver;

    // Constructor

    public BasePage() {

        driver = DriverFactory.getDriver();

    }

}
