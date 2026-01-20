
package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class NewslettersPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By newslettersForm = By.id("newsletters-form");
    private By firstNewsletterCheckbox = By.xpath(".//div/div[1]/div/div[2]/label[1]");
    private By emailInput = By.xpath("//*[@id='register-newsletters-form']/div[1]/input");
    private By continueButton = By.xpath("//input[@type='submit' and @value='Continue']");
    private By loginLink = By.linkText("Log In");
    private By loginEmail = By.id("email");
    private By loginPassword = By.id("password");
    private By loginSubmit = By.id("submit");

    public NewslettersPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void subscribe(String email, String password) {
        WebElement form = wait.until(ExpectedConditions.visibilityOfElementLocated(newslettersForm));
        form.findElement(firstNewsletterCheckbox).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput)).sendKeys(email);
        wait.until(ExpectedConditions.elementToBeClickable(continueButton)).click();

        wait.until(ExpectedConditions.elementToBeClickable(loginLink)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginEmail)).sendKeys(email);
        driver.findElement(loginPassword).sendKeys(password);
        driver.findElement(loginSubmit).click();
    }

    public void resubscribe(String email) {
        WebElement form = wait.until(ExpectedConditions.visibilityOfElementLocated(newslettersForm));
        WebElement newsletter = form.findElement(firstNewsletterCheckbox);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", newsletter);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", newsletter);

        wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput)).sendKeys(email);
        wait.until(ExpectedConditions.elementToBeClickable(continueButton)).click();
    }

    public void uncheckAllCheckedNewsletters() {
        WebElement newsletterForms = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("gigya-form")));
        List<WebElement> checkedCheckboxes = newsletterForms.findElements(
                By.cssSelector("input.gigyasdk__newsletter_checkbox:checked")
        );

        for (WebElement checkbox : checkedCheckboxes) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", checkbox);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
        }
    }

    public boolean isEmailFormDisplayed() {
    
            WebElement form = wait.until(ExpectedConditions.visibilityOfElementLocated(newslettersForm));
            return form.findElement(emailInput).isDisplayed();
        }
    
    }
