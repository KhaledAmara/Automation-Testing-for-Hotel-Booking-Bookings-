
package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pages.DetailsPage;
import pages.HomePage;
import pages.SearchResultPage;

public class BaseTest {

    protected WebDriver driver;
    protected HomePage homePage;
    protected SearchResultPage searchResultPage;
    protected DetailsPage detailsPage;

    @BeforeMethod
    public void setup() {
        WebDriverManager.edgedriver().setup();
        driver = new EdgeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.booking.com");

        homePage = new HomePage(driver);
        searchResultPage = new SearchResultPage(driver);
        detailsPage = new DetailsPage(driver);

        System.out.println("Browser opened and navigated to Booking.com homepage.");
    }

    @AfterMethod
    public void teardown() {
        if (driver != null) {
            driver.quit();
            System.out.println("Browser closed.");
        }
    }
}