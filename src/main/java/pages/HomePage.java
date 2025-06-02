package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

// HomePage now extends BasePage
public class HomePage extends BasePage {

    // Locators (optimized for stability)
    private final By locationInput = By.cssSelector("input[name='ss']");
    private final By dateRangeDisplayButton = By.cssSelector("button[data-testid='searchbox-dates-container']");
    private final By datePickerContainer = By.cssSelector("[data-testid='datepicker-tabs']");
    private final By searchButton = By.xpath("//button[./span[text()='Search']]");

    // MODIFIED LOCATOR FOR THE RIGHT ARROW BUTTON (VERIFY THIS ON BOOKING.COM'S BUTTON ELEMENT)
    private final By nextMonthButton = By.cssSelector("button[aria-label='Next month']");

    // MODIFIED LOCATOR FOR THE MONTH DISPLAY
    private final By monthDisplay = By.cssSelector("h3[aria-live='polite']");

    // Constructor now calls the BasePage constructor
    public HomePage(WebDriver driver) {
        super(driver); // Pass the driver to the BasePage constructor
    }

    /**
     * Enters search details and selects dates from Excel
     * @param location City name from Excel (e.g., "Alexandria")
     * @param excelCheckIn Date in dd-MMM-yyyy format from Excel (e.g., "01-Oct-2025")
     * @param excelCheckOut Date in dd-MMM-yyyy format from Excel (e.g., "14-Oct-2025")
     */
    public void searchHotel(String location, String excelCheckIn, String excelCheckOut) {
        enterLocation(location);
        openDatePicker();
        // The target month and year should ideally be derived from excelCheckIn or passed dynamically
        // For now, keeping "October 2025" as per original code, but consider making it dynamic.
        navigateToMonth("October 2025");
        selectDates(excelCheckIn, excelCheckOut);
        // Re-open date picker if it closes after date selection and before search button is clickable
        // This step might be redundant if the search button is immediately available.
        openDatePicker();
        clickSearch();
    }

    private void enterLocation(String location) {
        // Using inherited waitForVisibility method and then sendKeys
        waitForVisibility(locationInput, 10).sendKeys(location);
    }

    private void openDatePicker() {
        // Using inherited clickElement method
        clickElement(dateRangeDisplayButton, 10);
    }

    private void navigateToMonth(String targetMonthYear) {
        // Using inherited waitForVisibility method
        waitForVisibility(datePickerContainer, 10);

        // Loop until the target month and year are visible
        while (true) {
            String currentMonthText = driver.findElement(monthDisplay).getText();
            if (currentMonthText.contains(targetMonthYear)) {
                break; // Exit loop if target month is found
            }
            // Using inherited clickElement method
            clickElement(nextMonthButton, 5);
        }
    }

    private void selectDates(String excelCheckIn, String excelCheckOut) {
        clickDate(getDateLocator(excelCheckIn)); // Check-in
        clickDate(getDateLocator(excelCheckOut)); // Check-out
    }

    private By getDateLocator(String ddMMMyyyyDate) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate date = LocalDate.parse(ddMMMyyyyDate, inputFormatter);
        String formattedDate = outputFormatter.format(date);

        return By.cssSelector("[data-date='" + formattedDate + "']");
    }

    private void clickDate(By dateLocator) {
        // Using inherited clickElement method
        clickElement(dateLocator, 5);
    }

    private void clickSearch() {
        // Using inherited clickElement method
        clickElement(searchButton, 20);
    }
}
