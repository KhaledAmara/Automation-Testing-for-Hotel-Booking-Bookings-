package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.TimeoutException; // Import TimeoutException
import org.openqa.selenium.JavascriptExecutor; // Import JavascriptExecutor
import java.time.Duration;

// DetailsPage now extends BasePage
public class DetailsPage extends BasePage {

    // Locators for elements on the Details Page
    private final By checkInDateDisplay = By.cssSelector("button[data-testid='date-display-field-start']");
    private final By checkOutDateDisplay = By.cssSelector("button[data-testid='date-display-field-end']");

    private final By twinBedsRadio = By.xpath("//input[@type='radio' and @value='1' and contains(@name, 'bedPreference')]");
    private final By largeDoubleBedRadio = By.xpath("//input[@type='radio' and @value='2' and contains(@name, 'bedPreference')]");

    private final By roomAmountDropdown = By.cssSelector("select[data-testid='select-room-trigger']");

    // Locator for the "I'll reserve" button - now uncommented and used
    private final By iWillReserveButton = By.cssSelector("button.txp-bui-main-pp.bui-button--primary.js-reservation-button");


    // Constructor now calls the BasePage constructor
    public DetailsPage(WebDriver driver) {
        super(driver); // Pass the driver to the BasePage constructor
    }

    /**
     * Asserts that the displayed check-in and check-out dates match the expected dates.
     */
    public boolean verifyDisplayedDates(String expectedCheckInDisplayFormat, String expectedCheckOutDisplayFormat) {
        // Using inherited waitForVisibility method
        String actualCheckInText = waitForVisibility(checkInDateDisplay, 10).getText().trim();
        System.out.println("Displayed Check-in Date: " + actualCheckInText);

        String actualCheckOutText = waitForVisibility(checkOutDateDisplay, 10).getText().trim();
        System.out.println("Displayed Check-out Date: " + actualCheckOutText);

        boolean checkInMatch = actualCheckInText.contains(expectedCheckInDisplayFormat);
        boolean checkOutMatch = actualCheckOutText.contains(expectedCheckOutDisplayFormat);

        return checkInMatch && checkOutMatch;
    }

    /**
     * Selects a bed type if specified.
     * @param bedType "Twin" (for 2 single beds) or "Double" (for 1 large double bed) (case-insensitive)
     */
    public void selectBedType(String bedType) {
        WebElement bedOption = null;

        if (bedType != null && bedType.equalsIgnoreCase("Twin")) {
            // Using inherited clickElement method
            bedOption = clickElement(twinBedsRadio, 10);
        } else if (bedType != null && bedType.equalsIgnoreCase("Double")) {
            // Using inherited clickElement method
            bedOption = clickElement(largeDoubleBedRadio, 10);
        }


    }
    /**
     * Clicks the "I'll reserve" button.
     * Attempts a standard Selenium click first, falls back to JavaScript click if timed out.
     */
    private void clickIWillReserveButton() {
        try {
            // Attempt standard Selenium click using inherited method
            clickElement(iWillReserveButton, 15);
        } catch (TimeoutException e) {
            // Fallback to JavaScript click
            WebElement button = driver.findElement(iWillReserveButton);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", button);
        }
    }

    /**
     * Selects the specified amount of rooms and clicks the "I'll reserve" button.
     */
    public void selectRoomAmountAndReserve(String amount) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement amountDropdownElement = wait.until(ExpectedConditions.elementToBeClickable(roomAmountDropdown));
        Select selectAmount = new Select(amountDropdownElement);
        selectAmount.selectByValue(amount);
        System.out.println("Selected " + amount + " room(s).");
        try {
            Thread.sleep(5000); // 5000 milliseconds = 5 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Now call the clickIWillReserveButton method
        clickIWillReserveButton();

        // Wait for the button click to process and navigate to the last page
        try {
            Thread.sleep(15000); // 5000 milliseconds = 5 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }
}
