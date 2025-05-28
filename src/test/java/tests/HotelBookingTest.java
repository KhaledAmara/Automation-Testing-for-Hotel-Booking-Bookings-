

package tests;
import Data.ExcelReader;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map; // IMPORTANT: Import Map

public class HotelBookingTest extends BaseTest {

    // Define the path to your Excel file
    private static final String EXCEL_FILE_PATH = System.getProperty("user.dir") + File.separator + "src" +
            File.separator + "test" + File.separator + "resources" + File.separator + "testdata.xlsx";
    private static final String SHEET_NAME = "Sheet1"; // Ensure "Sheet1" matches your Excel sheet name

    @DataProvider(name = "ExcelData")
    public Object[][] bookingData() throws IOException{
        // get data from ExcelReader class
        ExcelReader ER = new ExcelReader();
        try {
            return ER.getExcelData();
        } catch (IOException e) {
            System.out.println("Error Occured " + e.getMessage());
            System.exit(0);
        }
        return ER.getExcelData();
    }

    @Test(dataProvider = "ExcelData")
    // IMPORTANT: Change the parameter to accept a single Map<String, String>
    public void testFullHotelBookingFlow(String location,String checkInDate, String checkOutDate, String bedType, String roomAmount, String expectedCheckInDisplay, String expectedCheckOutDisplay ) {
        // Retrieve values from the Map using column headers as keys.
        // Ensure these keys EXACTLY match your Excel column headers.


        String targetHotelName = "Tolip Hotel Alexandria";
        String expectedHotelUrlPart = "royal-tulip-alexandria";

        System.out.println("\n--- Starting full booking flow test with data ---");
        System.out.println("  Location: " + location);
        System.out.println("  Check-in: " + checkInDate);
        System.out.println("  Check-out: " + checkOutDate);
        System.out.println("  Bed Type: " + bedType);
        System.out.println("  Room Amount: " + roomAmount);
        System.out.println("  Expected Check-in Display: " + expectedCheckInDisplay);
        System.out.println("  Expected Check-out Display: " + expectedCheckOutDisplay);


        homePage.searchHotel(location, checkInDate, checkOutDate);
        System.out.println("Successfully performed search on HomePage. Now on Search Results Page.");

        String clickedHotelName = searchResultPage.proceedToHotelDetails();
        System.out.println("Attempted to click availability for: " + clickedHotelName);

        // Assert navigation to the hotel's details page
        Assert.assertTrue(searchResultPage.isOnHotelDetailsPage(expectedHotelUrlPart),
                "Expected to navigate to " + targetHotelName + " details page, but URL is: " + driver.getCurrentUrl());
        System.out.println("Successfully navigated to " + targetHotelName + " details page.");

        // Interact with the Details Page
        Assert.assertTrue(detailsPage.verifyDisplayedDates(expectedCheckInDisplay, expectedCheckOutDisplay),
                "Displayed check-in/out dates do not match expected.");
        System.out.println("Dates verified on details page.");

        detailsPage.selectBedType(bedType);
// Clean up roomAmount if it's something like "1.0"
        if (roomAmount.endsWith(".0")) {
            roomAmount = roomAmount.substring(0, roomAmount.length() - 2);
        }
        detailsPage.selectRoomAmountAndReserve(roomAmount);


        System.out.println("Selected bed type and room amount, attempted to click 'I'll reserve'.");

        System.out.println("Full booking flow test completed up to 'I'll reserve' click.");
    }
}




