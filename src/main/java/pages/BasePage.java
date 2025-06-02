package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor; // Import JavascriptExecutor
import org.openqa.selenium.WebDriverException; // Import WebDriverException

import java.time.Duration;

public class BasePage {

    protected WebDriver driver; // WebDriver instance, accessible by child classes

    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Helper method to wait for an element to be clickable and then click it.
     * If a standard click times out, it attempts a JavaScript click as a fallback.
     * @param locator The By locator for the element.
     * @param timeoutInSeconds The maximum time to wait for the element to be clickable.
     * @return The clicked WebElement.
     */
    protected WebElement clickElement(By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        WebElement element = null;

        try {
            // Attempt standard Selenium click
            element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            element.click();
        } catch (TimeoutException e) {
            try {
                // Fallback to JavaScript click if standard click fails
                element = driver.findElement(locator); // Find the element again, it might be present but not clickable
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", element);
            } catch (NoSuchElementException noSuchElementE) {
                throw new WebDriverException("Element not found for click: " + locator.toString(), noSuchElementE);
            } catch (Exception jsClickE) {
                throw new WebDriverException("Failed to click element " + locator.toString() + " even with JavaScript.", jsClickE);
            }
        } catch (Exception e) {
            throw new WebDriverException("Unexpected error during click operation for " + locator.toString(), e);
        }
        return element;
    }

    /**
     * Helper method to wait for an element to be visible.
     * @param locator The By locator for the element.
     * @param timeoutInSeconds The maximum time to wait for the element to be visible.
     * @return The visible WebElement.
     */
    protected WebElement waitForVisibility(By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Conditionally closes the sign-in information pop-up if it appears.
     * This method is now generic and can be used by any page object.
     */
    public void closePopupIfPresent() {
        // Locator for the sign-in information pop-up's close button
        By closeButton = By.cssSelector("button[aria-label=\"Dismiss sign in information.\"]");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Max wait for popup

        try {
            WebElement popupCloseButton = wait.until(ExpectedConditions.elementToBeClickable(closeButton));
            popupCloseButton.click();
        } catch (TimeoutException e) {
        } catch (NoSuchElementException e) {
            System.out.println("Pop-up element not found. Proceeding without closing.");
        }
    }
}
