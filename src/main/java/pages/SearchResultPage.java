package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import org.openqa.selenium.JavascriptExecutor;

public class SearchResultPage {
    private final WebDriver driver;

    // Locator for the specific Tolip Hotel Alexandria title on the search results page
    private final By hotelTitle = By.xpath("//div[@data-testid='property-card']//div[@data-testid='title' and contains(., 'Tolip Hotel Alexandria')]");

    // Locator for the "See availability" button for Tolip Alexandria Hotel
    // Uses contains(@href, 'royal-tulip-alexandria') to specifically target the correct hotel
    private final By availabilityButton = By.xpath("//a[@data-testid='availability-cta-btn'][contains(@href, 'royal-tulip-alexandria')]");

    // Locator for the sign-in information pop-up's close button
    private final By closeButton = By.cssSelector("button[aria-label=\"Dismiss sign in information.\"]");

    public SearchResultPage(WebDriver driver) {
        this.driver = driver;
    }

    // Method to get the text of the hotel title
    public String getHotelTitleText() {
        return new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.visibilityOfElementLocated(hotelTitle))
                .getText();
    }

    // Method to click the "See availability" button, forcing it to open in the same window
    private void clickAvailability() {
        System.out.println("Attempting to click 'See availability' button.");
        // Wait for the button to be clickable
        WebElement buttonToClick = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(availabilityButton));

        // Use JavascriptExecutor to remove the 'target' attribute
        // This prevents the link from opening in a new tab/window
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].removeAttribute('target')", buttonToClick);
        System.out.println("Removed 'target' attribute from the availability button.");

        // Click the button
        buttonToClick.click();
        System.out.println("Clicked 'See availability' button.");
    }

    // Method to conditionally close the pop-up
    public void closePopupIfPresent() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Max wait for popup
        try {
            WebElement popupCloseButton = wait.until(ExpectedConditions.elementToBeClickable(closeButton));
            popupCloseButton.click();
            System.out.println("Pop-up was present and closed successfully.");
        } catch (TimeoutException e) {
            System.out.println("Pop-up did not appear or was not clickable within 10 seconds. Proceeding without closing.");
        } catch (NoSuchElementException e) {
            System.out.println("Pop-up element not found. Proceeding without closing.");
        }
    }

    // Main method to proceed from search results to hotel details
    public String proceedToHotelDetails() {
        // Attempt to close the pop-up first
        closePopupIfPresent();

        // Get the hotel title for verification (optional, but good for context)
        String title = getHotelTitleText();
        System.out.println("Found hotel title on search results: " + title);

        // Click the availability button, which now ensures navigation in the same window
        clickAvailability();

        return title; // Return the title for potential assertions in your test class
    }

    // Method to verify if the current URL matches the expected hotel details page
    public boolean isOnHotelDetailsPage(String expectedHotelPartialUrl) {
        System.out.println("Verifying navigation to hotel details page. Expected URL part: " + expectedHotelPartialUrl);
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