package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class DetailsPage {

    private final WebDriver driver;

    // Locators for elements on the Details Page
    private final By checkInDateDisplay = By.cssSelector("button[data-testid='date-display-field-start']");
    private final By checkOutDateDisplay = By.cssSelector("button[data-testid='date-display-field-end']");

    private final By twinBedsRadio = By.xpath("//input[@type='radio' and @value='1' and contains(@name, 'bedPreference')]");
    private final By largeDoubleBedRadio = By.xpath("//input[@type='radio' and @value='2' and contains(@name, 'bedPreference')]");

    private final By roomAmountDropdown = By.cssSelector("select[data-testid='select-room-trigger']");

    // This is the locator as it was when it was causing TimeoutException on elementToBeClickable
    private final By reserveButtonLocator = By.cssSelector("button.txp-bui-main-pp.bui-button--primary.js-reservation-button");


    public DetailsPage(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Asserts that the displayed check-in and check-out dates match the expected dates.
     */
    public boolean verifyDisplayedDates(String expectedCheckInDisplayFormat, String expectedCheckOutDisplayFormat) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        String actualCheckInText = wait.until(ExpectedConditions.visibilityOfElementLocated(checkInDateDisplay)).getText().trim();
        System.out.println("Displayed Check-in Date: " + actualCheckInText);

        String actualCheckOutText = wait.until(ExpectedConditions.visibilityOfElementLocated(checkOutDateDisplay)).getText().trim();
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
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement bedOption = null;

        if (bedType != null && bedType.equalsIgnoreCase("Twin")) {
            bedOption = wait.until(ExpectedConditions.elementToBeClickable(twinBedsRadio));
        } else if (bedType != null && bedType.equalsIgnoreCase("Double")) {
            bedOption = wait.until(ExpectedConditions.elementToBeClickable(largeDoubleBedRadio));
        } else {
            System.out.println("No specific bed type requested or invalid bed type: " + bedType + ".");
            return;
        }

        if (bedOption != null && !bedOption.isSelected()) {
            bedOption.click();
            System.out.println("Selected bed type: " + bedType);
        }
    }

    /**
     * Clicks the "I'll reserve" button.
     */
    private void clickIWillReserveButton() {
        // This is the line that was failing with TimeoutException
        new WebDriverWait(driver, Duration.ofSeconds(15)) // Increased wait time to 15 seconds
                .until(ExpectedConditions.elementToBeClickable(reserveButtonLocator))
                .click();
        System.out.println("Clicked 'I'll reserve' button.");
    }

    /**
     * Selects the specified amount of rooms and clicks the "I'll reserve" button.
     */
    public void selectRoomAmountAndReserve(String amount) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement amountDropdownElement = wait.until(ExpectedConditions.elementToBeClickable(roomAmountDropdown));
        Select selectAmount = new Select(amountDropdownElement);
        selectAmount.selectByValue(amount);
        System.out.println("Selected " + amount + " room(s).");


        clickIWillReserveButton();


        try {
            System.out.println("Waiting for 10 seconds...");
            Thread.sleep(10000); // 5000 milliseconds = 5 seconds
        } catch (InterruptedException e) {
            System.err.println("Thread sleep interrupted: " + e.getMessage());
            // It's good practice to restore the interrupted status if the interruption is unexpected
            Thread.currentThread().interrupt();
        }
    }


}