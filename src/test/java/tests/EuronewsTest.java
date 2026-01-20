package tests;

import helpers.GmailApiHelper;
import helpers.GmailTokenHelper;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.ConfirmationPage;
import pages.HomePage;
import pages.NewslettersPage;
import helpers.ConfigReader;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EuronewsTest {

    private WebDriver driver;
    private GmailApiHelper gmailHelper;
    private final String newsletterSubject = "Confirm your subscription";
    private String testEmail;
    private String testPassword;

    @BeforeEach
    public void setup() throws Exception {
        testEmail = ConfigReader.get("test.email");
        testPassword = ConfigReader.get("test.password");
        System.setProperty("webdriver.chrome.driver",
                "C:\\tools\\chromedriver\\chromedriver-win64\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications", "--disable-geolocation", "--start-maximized");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

       String accessToken = GmailTokenHelper.getAccessToken();
        gmailHelper = new GmailApiHelper(accessToken);
    }

    @Test
    public void euronewsNewsletterFlowTest() throws Exception {
        HomePage home = new HomePage(driver);
        NewslettersPage newsletters = new NewslettersPage(driver);
        ConfirmationPage confirmation = new ConfirmationPage(driver);

        home.open("https://www.euronews.com/");
        home.acceptCookies();

        assertTrue(driver.getTitle().contains("Euronews"), "Main page did not open correctly");
        home.goToNewsletters();
       assertTrue(driver.getCurrentUrl().contains("/newsletters"), "Newsletters page did not open");
       newsletters.subscribe(testEmail, testPassword);
       Thread.sleep(8000);
        boolean subscriptionEmailReceived = false;
        for (int i = 0; i < 12; i++) {
            if (gmailHelper.isEmailPresent(newsletterSubject)) {
                subscriptionEmailReceived = true;
                break;
            }
            System.out.println("Waiting for subscription email... attempt " + (i + 1));
            Thread.sleep(5000);
        }
        assertTrue(subscriptionEmailReceived, "Subscription confirmation email was not received");
        confirmation.backToHome();
        assertTrue(driver.getTitle().contains("Euronews"), "Did not navigate back to main page");

        home.goToNewsletters();
        newsletters.resubscribe(testEmail);
        newsletters.uncheckAllCheckedNewsletters();

        Thread.sleep(8000);
        boolean unsubscribeEmailReceived = gmailHelper.isEmailPresent("subscription canceled");
        assertTrue(!unsubscribeEmailReceived, "Unsubscribe confirmation email was received");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}
