# Wikipedia Selenium Testing

## Overview

This project is a Selenium-based automated testing suite designed to validate core functionality and accessibility of the Wikipedia website. The framework uses Java, Selenium WebDriver, TestNG, and Maven and follows the Page Object Model (POM) design pattern for maintainability and scalability.

The test suite verifies multiple Wikipedia features including login navigation, article structure, search functionality, categories, navigation elements, and special pages. In addition to functional testing, the project integrates automated accessibility scanning using Axe-core to evaluate compliance with WCAG accessibility standards.

The system includes 40 automated test cases across 8 modules, each validating different aspects of the website.

<br>

## Test Coverage
### Total Tests

8 Modules × 5 Test Cases = 40 Tests

| Module                   | Test Cases                                                                                                                                                                                                        |
| ------------------------ | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Login Module**         | TC-L01: Form elements present<br>TC-L02: Invalid credentials error<br>TC-L03: Forgot password navigation<br>TC-L04: Create Account link navigation<br>TC-L05: WCAG accessibility scan                             |
| **Article Module**       | TC-A01: Article loads with table of contents<br>TC-A02: Infobox present<br>TC-A03: References section present<br>TC-A04: Internal links navigate correctly<br>TC-A05: WCAG accessibility scan                     |
| **Search Module**        | TC-S01: Valid search query returns results<br>TC-S02: Nonsense query returns no results<br>TC-S03: Search from homepage<br>TC-S04: Clicking result opens article<br>TC-S05: WCAG accessibility scan               |
| **Home Page Module**     | TC-H01: Logo and title load<br>TC-H02: Featured / News / Did You Know sections<br>TC-H03: On This Day section<br>TC-H04: Random Article link<br>TC-H05: WCAG accessibility scan                                   |
| **Category Module**      | TC-C01: Category heading correct<br>TC-C02: Articles section contains links<br>TC-C03: Subcategories present<br>TC-C04: Clicking article opens it<br>TC-C05: WCAG accessibility scan                              |
| **History Module**       | TC-HI01: Revision history loads<br>TC-HI02: Revision dates present<br>TC-HI03: Contributor links present<br>TC-HI04: Compare revisions button present<br>TC-HI05: WCAG accessibility scan                         |
| **Navigation Module**    | TC-N01: Logo click returns to homepage<br>TC-N02: Footer contains navigation links<br>TC-N03: Random Article link in sidebar<br>TC-N04: Edit and History tabs on article pages<br>TC-N05: WCAG accessibility scan |
| **Special Pages Module** | TC-SP01: Special pages list loads<br>TC-SP02: Recent changes page loads<br>TC-SP03: Statistics table present<br>TC-SP04: Random page redirects to article<br>TC-SP05: WCAG accessibility scan                     |

<br>

## Architecture
### Page Object Model (POM)

The framework follows the Page Object Model design pattern to separate test logic from UI interactions.

### Base Page

* BasePage

  * Common WebDriver functions

  * Shared element interaction utilities

### Page Objects

* HomePage

* ArticlePage

* SearchPage

* LoginPage

* CreateAccountPage

* CategoryPage

* HistoryPage

* NavigationPage

* SpecialPage

* TalkPage

<br>

Each page object encapsulates:

* Locators

* Page actions

* Validation methods

<br>

## Utilities
### DriverManager

* Manages WebDriver instances

* Uses ThreadLocal WebDriver for parallel test execution

### WaitUtil

* Implements explicit waits

* Prevents synchronization issues with dynamic page elements

### AxeAccessibilityUtil

* Runs automated WCAG accessibility scans

* Generates structured JSON reports

<br>

## Accessibility Testing

Accessibility testing is implemented using axe-core integration. <br>

Each module contains a dedicated TestNG test:

```@Test(groups = "accessibility")```

This test performs a WCAG accessibility scan of the current page and generates a report.

### Accessibility Reports

Reports are automatically saved as timestamped JSON files in:

```target/wcag-reports/```

These reports include:

* Accessibility violations

* Impact severity

* Element locations

* Suggested fixes

<br>

## Technology Stack
| Technology         | Purpose                             |
| ------------------ | ----------------------------------- |
| Java 25            | Programming language                |
| Selenium WebDriver | Browser automation                  |
| TestNG             | Test framework                      |
| Maven              | Dependency management & build       |
| Axe-core           | Accessibility testing               |
| WebDriverManager   | Automatic browser driver management |

<br>

## Project Structure
```
wikipedia-selenium-tests
│
├── src
│   ├── main
│   │   └── java
│   │       └── com.wikipedia
│   │           ├── pages
│   │           │   ├── ArticlePage.java
│   │           │   ├── BasePage.java
│   │           │   ├── CategoryPage.java
│   │           │   ├── CreateAccountPage.java
│   │           │   ├── HistoryPage.java
│   │           │   ├── HomePage.java
│   │           │   ├── LoginPage.java
│   │           │   ├── NavigationPage.java
│   │           │   ├── SearchPage.java
│   │           │   ├── SpecialPage.java
│   │           │   └── TalkPage.java
│   │           │
│   │           └── utils
│   │               ├── AxeAccessibilityUtil.java
│   │               ├── DriverManager.java
│   │               └── WaitUtil.java
│   │             
│   └── test
│       ├── java
│       │    └── com.wikipedia.tests
│       │        ├── ArticleModuleTest.java
│       │        ├── BaseTest.java
│       │        ├── CategoryModuleTest.java
│       │        ├── HistoryModuleTest.java
│       │        ├── HomePageModuleTest.java
│       │        ├── LoginModuleTest.java
│       │        ├── NavigationModuleTest.java
│       │        ├── SearchModuleTest.java
│       │        └── SpecialPagesModuleTest.java
│       │        
│       └── resources
│           └── testng.xml
├── target
│   ├── clasess
|   
│
├── pom.xml
└── README.md
```

<br>

## Installation
### Prerequisites

* Java 11 or higher (Java 25 preferred)

* Maven

* Chrome or Firefox browser

* Internet access to download dependencies from Maven Central

<br>

## Setup

### Setup Version 1:
Clone the repository:

```git clone https://github.com/yourusername/wikipedia-selenium-tests.git```

Navigate into the project directory:

```cd wikipedia-selenium-tests```

Install dependencies:

```mvn clean install```

<br>

#### Running Tests

Run all tests:

```mvn test```

Run only accessibility tests:

```mvn test -Dgroups=accessibility```

<br>

### Setup Version 2
Download the wikipedia-selenium-tests.zip file.

Unzip/extract the file into a new folder.

Open IntelliJ (or any other IDE, but IntelliJ is preferred) and open the folder containing the extracted zip files.

IntelliJ will automatically detect Maven from the pom.xml file.

Navigate to /src/test/resources and right-click on testng.xml. Click on *'Run ...\wikipedia-selenium\src\test\resources\testng.xml'* to execute the program.

<br>

#### Test Execution Output

After test execution:

Test results: ```target/surefire-reports```

Accessibility reports: ```target/wcag-reports```

<br>


## Future Improvements

Possible enhancements include:

* Adding HTML accessibility reports

* Implementing parallel cross-browser testing

* Adding CI/CD integration (GitHub Actions or Jenkins)

* Capturing screenshots on test failures

* Adding visual regression testing
