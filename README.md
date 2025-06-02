# Booking.com Hotel Booking Automation

## Project Overview

This project provides automated test scripts for key functionalities on Booking.com, specifically focusing on the hotel search and booking flow. It leverages Selenium WebDriver with Java and TestNG, following the Page Object Model (POM) design pattern for maintainability and scalability. Test data is driven by an Excel file.

## Features

* **Hotel Search:** Search for hotels by location.
* **Date Selection:** Select check-in and check-out dates using the interactive calendar.
* **Dynamic Element Handling:** Includes robust methods to handle dynamic elements such as sign-in pop-ups.
* **Search Results Navigation:** Navigates search results to find a specific hotel ("Tolip Hotel Alexandria").
* **Hotel Details Interaction:** Proceeds to the hotel details page, verifies displayed dates, selects bed types, and specifies room amounts.
* **Data-Driven Testing:** Utilizes Apache POI to read test data from an Excel file (`testdata.xlsx`).
* **Robust Element Interaction:** Includes a fallback mechanism (JavaScript click) for problematic button clicks to ensure test stability.
* **Browser Management:** Manages WebDriver setup and teardown efficiently.

## Technologies Used

* **Language:** Java (JDK 21)
* **Automation Framework:** Selenium WebDriver
* **Testing Framework:** TestNG
* **Build Tool:** Apache Maven
* **WebDriver Management:** WebDriverManager
* **Data Handling:** Apache POI (for Excel)
* **Browser under Test:** Microsoft Edge

## Project Structure

.
├── src/
│   ├── main/java/pages/            # Contains Page Object classes:
│   │   ├── BasePage.java           # Base class for all page objects, handling common WebDriver operations and robust clicking.
│   │   ├── HomePage.java           # Interactions with the Booking.com home page.
│   │   ├── DetailsPage.java        # Interactions with the hotel details page.
│   │   └── SearchResultPage.java   # Interactions with the hotel search results page.
│   └── test/
│       ├── java/Data/              # Contains data management classes:
│       │   └── ExcelReader.java    # Utility for reading test data from Excel.
│       └── java/tests/             # Contains TestNG test classes:
│           ├── BaseTest.java       # Base class for all tests, handling WebDriver setup and teardown.
│           └── HotelBookingTest.java # Main test class for the hotel booking flow.
├── src/test/resources/           # Contains test data:
│   └── testdata.xlsx             # Excel file storing test input data.
├── pom.xml                       # Maven Project Object Model file (manages dependencies and build process).
└── README.md                     # Project documentation (this file).


## Prerequisites

Before running this project, ensure you have the following installed:

* **Java Development Kit (JDK):** Version 21 or higher.
* **Apache Maven:** For project build and dependency management.
* **Microsoft Edge Browser:** The project is configured to run tests on Microsoft Edge. WebDriverManager will handle the driver automatically.
* **IntelliJ IDEA (Recommended IDE):** Or any other compatible Java IDE.

## Installation & Setup

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/KhaledAmara/Automation-Testing-for-Hotel-Bookings.git](https://github.com/KhaledAmara/Automation-Testing-for-Hotel-Bookings.git)
    cd Automation-Testing-for-Hotel-Bookings
    ```
2.  **Import into IDE:** Open IntelliJ IDEA and import the cloned directory as an existing Maven project.
3.  **Update Maven Dependencies:** Your IDE should automatically detect the `pom.xml` file and prompt you to import/download necessary Maven dependencies. If not, manually refresh Maven dependencies.
4.  **Test Data:** Ensure your `testdata.xlsx` file is located in `src/test/resources/` and its content matches the expected format for the `HotelBookingTest`.

## How to Run Tests

### Via Maven Command Line:

Navigate to the project root directory in your terminal or command prompt and execute:

```bash
mvn clean test
Via IntelliJ IDEA:
Open the HotelBookingTest.java file located under src/test/java/tests/.
Right-click anywhere within the testFullHotelBookingFlow method or on the class name itself.
Select "Run 'testFullHotelBookingFlow'" from the context menu.
Test Data (testdata.xlsx)
The testdata.xlsx file located in src/test/resources/ is used to feed data into the HotelBookingTest. Ensure the column headers and data types in your Excel file correspond to the parameters expected by the @DataProvider in HotelBookingTest. The current test expects data for: location, checkInDate, checkOutDate, bedType, roomAmount, expectedCheckInDisplay, expectedCheckOutDisplay.

Author
Khaled Amara
