package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import org.openqa.selenium.JavascriptExecutor;

// SearchResultPage now extends BasePage
public class SearchResultPage extends BasePage {

    // Locator for the specific Tolip Hotel Alexandria title on the search results page
    private final By hotelTitle = By.xpath("//div[@data-testid='property-card']//div[@data-testid='title' and contains(., 'Tolip Hotel Alexandria')]");

    // Locator for a general property card to ensure search results are loaded
    private final By genericPropertyCard = By.cssSelector("div[data-testid='property-card']");

    // Locator for the "See availability" button for Tolip Alexandria Hotel
    // Uses contains(@href, 'royal-tulip-alexandria') to specifically target the correct hotel
    private final By availabilityButton = By.xpath("//a[@data-testid='availability-cta-btn'][contains(@href, 'royal-tulip-alexandria')]");

    // Constructor now calls the BasePage constructor
    public SearchResultPage(WebDriver driver) {
        super(driver); // Pass the driver to the BasePage constructor
    }

    // Method to get the text of the hotel title
    public String getHotelTitleText() {
        // First, wait for at least one property card to be visible to ensure results are loading
        waitForVisibility(genericPropertyCard, 25); // Increased wait for general results

        // Then, wait for the specific hotel title to be visible
        // Using inherited waitForVisibility method with increased timeout
        return waitForVisibility(hotelTitle, 30).getText(); // Increased wait to 30 seconds
    }

    // Method to click the "See availability" button, forcing it to open in the same window
    private void clickAvailability() {
        // First, wait for the element to be present in the DOM and visible
        WebElement buttonToClick = waitForVisibility(availabilityButton, 10);

        // Use JavascriptExecutor to remove the 'target' attribute BEFORE clicking
        // This prevents the link from opening in a new tab/window
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].removeAttribute('target')", buttonToClick);

        // Now click the button
        buttonToClick.click();
    }

    // Main method to proceed from search results to hotel details
    public String proceedToHotelDetails() {
        // Call the closePopupIfPresent method from BasePage
        closePopupIfPresent();

        // Get the hotel title for verification (optional, but good for context)
        String title = getHotelTitleText();

        // Click the availability button, which now ensures navigation in the same window
        clickAvailability();

        return title; // Return the title for potential assertions in your test class
    }

    // Method to verify if the current URL matches the expected hotel details page
    public boolean isOnHotelDetailsPage(String expectedHotelPartialUrl) {
        try {
            // Wait until the URL contains the expected part for the hotel details page
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // Increased wait time for navigation
            wait.until(ExpectedConditions.urlContains(expectedHotelPartialUrl));
            System.out.println("Current URL: " + driver.getCurrentUrl() + " contains expected part.");
            return true;
        } catch (Exception e) {
            System.out.println("URL did not contain expected hotel details part. Current URL: " + driver.getCurrentUrl());
            return false;
        }
    }
}
