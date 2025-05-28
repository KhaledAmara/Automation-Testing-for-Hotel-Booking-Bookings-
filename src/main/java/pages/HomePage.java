package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class HomePage {
    private final WebDriver driver;

    // Locators (optimized for stability)
    private final By locationInput = By.cssSelector("input[name='ss']");
    private final By dateRangeDisplayButton = By.cssSelector("button[data-testid='searchbox-dates-container']");
    private final By datePickerContainer = By.cssSelector("[data-testid='datepicker-tabs']");
    private final By searchButton = By.xpath("//button[./span[text()='Search']]");;

    // MODIFIED LOCATOR FOR THE RIGHT ARROW BUTTON (VERIFY THIS ON BOOKING.COM'S BUTTON ELEMENT)
    private final By nextMonthButton = By.cssSelector("button[aria-label='Next month']");

    // MODIFIED LOCATOR FOR THE MONTH DISPLAY
    private final By monthDisplay = By.cssSelector("h3[aria-live='polite']");

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Enters search details and selects dates from Excel
     * @param location City name from Excel (e.g., "Alexandria")
     * @param excelCheckIn Date in dd/MM/yyyy format from Excel (e.g., "01/10/2025")
     * @param excelCheckOut Date in dd/MM/yyyy format from Excel (e.g., "14/10/2025")
     */
    public void searchHotel(String location, String excelCheckIn, String excelCheckOut) {
        enterLocation(location);
        openDatePicker();
        navigateToMonth("October 2025"); // Target month and year
        selectDates(excelCheckIn, excelCheckOut);
        openDatePicker();
        clickSearch();
    }

    private void enterLocation(String location) {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(locationInput))
                .sendKeys(location);
    }

    private void openDatePicker() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(dateRangeDisplayButton))
                .click();
    }

    private void navigateToMonth(String targetMonthYear) {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(datePickerContainer));

        // Loop until the target month and year are visible
        while (true) {
            String currentMonthText = driver.findElement(monthDisplay).getText();
            if (currentMonthText.contains(targetMonthYear)) {
                break; // Exit loop if target month is found
            }
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(nextMonthButton))
                    .click();
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
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(dateLocator))
                .click();
    }

    private void clickSearch() {
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.elementToBeClickable(searchButton))
                .click();
    }
}