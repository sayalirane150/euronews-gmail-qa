package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ConfirmationPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By backToSiteLink = By.cssSelector("a.c-backto-lastvisitedpage");

    public ConfirmationPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void backToHome() {
        WebElement back = wait.until(ExpectedConditions.presenceOfElementLocated(backToSiteLink));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", back);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", back);
    }
}
