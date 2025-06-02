package tests;
import Data.ExcelReader;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.DetailsPage; // Import DetailsPage
import pages.HomePage;     // Import HomePage
import pages.SearchResultPage; // Import SearchResultPage

import java.io.File;
import java.io.IOException;

public class HotelBookingTest extends BaseTest {

    // Define the path to your Excel file
    private static final String EXCEL_FILE_PATH = System.getProperty("user.dir") + File.separator + "src" +
            File.separator + "test" + File.separator + "resources" + File.separator + "testdata.xlsx";
    private static final String SHEET_NAME = "Sheet1"; // Ensure "Sheet1" matches your Excel sheet name

    @DataProvider(name = "ExcelData")
    public Object[][] bookingData() throws IOException {
        // get data from ExcelReader class
        ExcelReader ER = new ExcelReader();
        try {
            return ER.getExcelData();
        } catch (IOException e) {
            System.out.println("Error Occurred: " + e.getMessage());
            System.exit(0); // Exit if file not found or other IO error
        }
        return ER.getExcelData(); // This line might be unreachable if System.exit(0) is called
    }

    @Test(dataProvider = "ExcelData")
    public void testFullHotelBookingFlow(String location, String checkInDate, String checkOutDate, String bedType, String roomAmount, String expectedCheckInDisplay, String expectedCheckOutDisplay) throws InterruptedException {
        // Instantiate page objects within the test method
        HomePage homePage = new HomePage(driver);
        SearchResultPage searchResultPage = new SearchResultPage(driver);
        DetailsPage detailsPage = new DetailsPage(driver);

        String targetHotelName = "Tolip Hotel Alexandria";
        String expectedHotelUrlPart = "royal-tulip-alexandria";

        homePage.searchHotel(location, checkInDate, checkOutDate);

        String clickedHotelName = searchResultPage.proceedToHotelDetails();

        // Assert navigation to the hotel's details page
        Assert.assertTrue(searchResultPage.isOnHotelDetailsPage(expectedHotelUrlPart),
                "Expected to navigate to " + targetHotelName + " details page, but URL is: " + driver.getCurrentUrl());

        // Interact with the Details Page
        Assert.assertTrue(detailsPage.verifyDisplayedDates(expectedCheckInDisplay, expectedCheckOutDisplay),
                "Displayed check-in/out dates do not match expected.");

        detailsPage.selectBedType(bedType);
        // Clean up roomAmount if it's something like "1.0"
        if (roomAmount.endsWith(".0")) {
            roomAmount = roomAmount.substring(0, roomAmount.length() - 2);
        }
        detailsPage.selectRoomAmountAndReserve(roomAmount);


    }
}
