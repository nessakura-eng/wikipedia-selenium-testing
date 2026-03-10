import org.openqa.selenium.By;

public class HomePage extends BasePage {

By searchBox = By.id("searchInput");
By searchButton = By.cssSelector("button[type='submit']");
By englishLink = By.id("js-link-box-en");

public void open(){

driver.get("https://www.wikipedia.org/");

}

public void search(String text){

driver.findElement(searchBox).sendKeys(text);
driver.findElement(searchButton).click();

}

public void openEnglish(){

driver.findElement(englishLink).click();

}

}
