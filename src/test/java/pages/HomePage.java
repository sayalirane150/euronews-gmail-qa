package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {
    private WebDriver driver;

    private By cookieAcceptBtn = By.id("didomi-notice-agree-button");
    private By newslettersLink = By.linkText("Newsletters");

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public void open(String url) {
        driver.get(url);
    }

    public void acceptCookies() {
        driver.findElement(cookieAcceptBtn).click();
    }

    public void goToNewsletters() {
        driver.findElement(newslettersLink).click();
    }
}
