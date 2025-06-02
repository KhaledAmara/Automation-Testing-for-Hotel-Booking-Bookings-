package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    protected WebDriver driver; // WebDriver instance, accessible by test classes

    @BeforeMethod
    public void setup() {
        // Setup EdgeDriver using WebDriverManager
        WebDriverManager.edgedriver().setup();
        // Initialize EdgeDriver
        driver = new EdgeDriver();
        // Maximize the browser window
        driver.manage().window().maximize();
        // Navigate to Booking.com homepage
        driver.get("https://www.booking.com");

    }

    @AfterMethod
    public void teardown() {
        // Close the browser if the driver instance is not null
        if (driver != null) {
            driver.quit();
        }
    }
}
