
# Selenium TestNG Project README

## Overview
This project contains automated tests for verifying web application functionality using **Selenium WebDriver** and **TestNG** on www.paycom.com

## Prerequisites
To run the tests, ensure the following are installed:
- **Java**: JDK 21 or higher
- **Maven**: For dependency management
- **Chrome Browser**: Latest stable version
- **TestNG**: Included via Maven dependencies
- **IDE**: IntelliJ IDEA, Eclipse, or any Java-compatible IDE
- **Git**: For version control

## Project Structure
```
├── src
│   ├── test
│   │   ├── java
│   │   │   └── HomePageLinksCheckerTest.java  # Test 
│   │   │   └── JobPostingsTest.java           # Test
├── pom.xml                                    # Maven configuration
├── .gitignore                                 # Git ignore file
└── README.md                                  # This file
```

## Test Details
- **Test Class**: `HomePageLinksCheckerTest`
- **Purpose**: Verifies that all footer navigation links (`<a>` tags in `#footer-nav`) return valid HTTP responses.
- **Key Features**:
    - Uses Selenium WebDriver to navigate to the specified URL and locate footer links.
    - Uses `HttpClient` to send HEAD requests to each link and validate status codes.
    - Optimized for performance with parallel HTTP requests and headless Chrome mode.
- **Output**:
    - TestNG reports are generated in the `test-output/` directory (excluded from Git via `.gitignore`).


- **Test Class**: `JobPostingsTest`
- **Purpose**: Verify the expected message is displayed when no jobs are found.
- **Key Features**:
  - Navigation: Uses Selenium to navigate from a homepage to a careers page and then to a job search page.
  - Explicit Waits: Employs WebDriverWait to handle dynamic page loading (e.g., waiting for title or element visibility).
  - XPath Locators: Uses XPath for locating elements like links and buttons based on text content.
  - Input Validation: Tests the search functionality with an invalid input ("Nonexisting") to verify error handling.
  - Assertions: Ensures critical steps (link validity, result message) meet expected conditions.
- **Output**:
    - TestNG reports are generated in the `test-output/` directory (excluded from Git via `.gitignore`).

