# Wikipedia Selenium Testing

## Overview

This project is a Selenium-based automated testing suite designed to validate core functionality and accessibility of the Wikipedia website. The framework uses Java, Selenium WebDriver, TestNG, and axe-core for comprehensive testing.

The test suite verifies multiple Wikipedia features including login navigation, article structure, search functionality, categories, navigation elements, and special pages. In addition to functional testing, every test module includes automated WCAG accessibility compliance checks.

The system includes 51 automated test cases across 8 modules, each validating different aspects of the website.

<br>

## Test Coverage
### Total Tests

8 Modules × 51 Total Test Cases

### Test Modules & Test Cases

| Module | Test Cases | Count |
| --- | --- | --- |
| **Login Module** | TC-L01: Form elements present<br>TC-L02: Invalid credentials error<br>TC-L03: Forgot password navigation<br>TC-L04: Create Account link navigation<br>TC-L05: WCAG accessibility compliance | 5 |
| **Article Module** | TC-A01: Article loads with table of contents & categories<br>TC-A02: Infobox present<br>TC-A03: References section present<br>TC-A04: Internal links navigate correctly<br>TC-A05: WCAG accessibility compliance<br>TC-A06: Article image opens and closes in Media Viewer | 6 |
| **Search Module** | TC-S01: Valid search query returns results<br>TC-S02: Nonsense query returns no results<br>TC-S03: Search autocomplete suggestions appear<br>TC-S04: Search results pagination works<br>TC-S05: WCAG accessibility compliance | 5 |
| **Home Page Module** | TC-H01: Logo and title load<br>TC-H02: Featured Article / News / Did You Know sections present<br>TC-H03: On This Day section present<br>TC-H04: Random Article link navigates<br>TC-H05: WCAG accessibility compliance | 5 |
| **Category Module** | TC-C01: Category page loads with correct URL<br>TC-C02: Articles section contains links<br>TC-C03: Subcategories present<br>TC-C04: Clicking article opens it<br>TC-C05: WCAG accessibility compliance | 5 |
| **History Module** | TC-HI01: Revision history loads<br>TC-HI02: Revision dates present<br>TC-HI03: Contributor links present<br>TC-HI04: Compare revisions button present<br>TC-HI05: WCAG accessibility compliance | 5 |
| **Navigation Module** | TC-N01: Logo click returns to homepage<br>TC-N02: Footer contains navigation links<br>TC-N03: Language selector switches language<br>TC-N04: Browser back/forward navigation works<br>TC-N05: WCAG accessibility compliance<br>TC-N06: Language input settings can be set to Korean | 6 |
| **Special Pages Module** | TC-SP01: Special pages list loads<br>TC-SP02: New Pages loads<br>TC-SP03: Statistics table present<br>TC-SP04: Wanted Pages loads<br>TC-SP05: WCAG accessibility compliance<br>TC-SP06: Tools menu contains expected links<br>TC-SP07: Appearance options can be toggled<br>TC-SP08: Cite this page tool opens<br>TC-SP09: Get shortened URL generates short URL<br>TC-SP10: Appearance settings cycle and apply<br>TC-SP11: Appearance settings persist across pages | 11 |

**Total Test Cases: 51**

<br>

## Test Module Details

### 1. Login Module (LoginModuleTest.java)
- **TC-L01:** Verify login page loads with all required form elements (username, password, submit button)
- **TC-L02:** Verify login is rejected for invalid credentials and error message is displayed
- **TC-L03:** Verify Forgot Password link navigates to the password reset page
- **TC-L04:** Verify Create Account link navigates to the registration/account creation page
- **TC-L05:** WCAG accessibility compliance scan of the login page with axe-core

### 2. Article Module (ArticleModuleTest.java)
- **TC-A01:** Verify article page loads with core elements: content, Table of Contents, and categories
- **TC-A02:** Verify infobox is displayed for notable subject articles (Python, Albert Einstein, etc.)
- **TC-A03:** Verify references section is present at the bottom of article pages
- **TC-A04:** Verify internal wiki links are clickable and navigate to other Wikipedia articles
- **TC-A05:** WCAG accessibility compliance scan of article pages with axe-core
- **TC-A06:** Verify article images open in fullscreen Media Viewer and can be closed with X button

### 3. Search Module (SearchModuleTest.java)
- **TC-S01:** Verify search with valid query returns results and navigates to relevant page
- **TC-S02:** Verify search with nonsense/invalid query returns no results message
- **TC-S03:** Verify search autocomplete suggestions appear while typing in search box
- **TC-S04:** Verify search results pagination navigates to next page of results
- **TC-S05:** WCAG accessibility compliance scan of search results page with axe-core

### 4. Home Page Module (HomePageModuleTest.java)
- **TC-H01:** Verify homepage loads with Wikipedia logo and search box
- **TC-H02:** Verify featured content sections present: Featured Article, In the News, Did You Know
- **TC-H03:** Verify On This Day section is present on homepage
- **TC-H04:** Verify Random Article link navigates to a valid Wikipedia article
- **TC-H05:** WCAG accessibility compliance scan of the homepage with axe-core

### 5. Category Module (CategoryModuleTest.java)
- **TC-C01:** Verify category page loads and URL contains Category: namespace
- **TC-C02:** Verify articles section is present with clickable article links
- **TC-C03:** Verify subcategories section is present on parent category pages
- **TC-C04:** Verify clicking a category article link opens the article page
- **TC-C05:** WCAG accessibility compliance scan of category pages with axe-core

### 6. History Module (HistoryModuleTest.java)
- **TC-HI01:** Verify article history page loads with revision list
- **TC-HI02:** Verify revision date links are present and clickable in history
- **TC-HI03:** Verify contributor links are present and navigable to user/contribution pages
- **TC-HI04:** Verify compare revisions button is present and clickable for revision comparison
- **TC-HI05:** WCAG accessibility compliance scan of history pages with axe-core

### 7. Navigation Module (NavigationModuleTest.java)
- **TC-N01:** Verify clicking Wikipedia logo returns to main page from any article
- **TC-N02:** Verify footer is present with navigation links to Wikimedia pages
- **TC-N03:** Verify language selector switches Wikipedia to different language versions
- **TC-N04:** Verify browser back/forward navigation works between pages
- **TC-N05:** WCAG accessibility compliance scan of About Wikipedia page with axe-core
- **TC-N06:** Verify language input settings can be set to Korean with keyboard selection

### 8. Special Pages Module (SpecialPagesModuleTest.java)
- **TC-SP01:** Verify Special:SpecialPages loads with list of special pages
- **TC-SP02:** Verify Special:NewPages loads with newly created pages list
- **TC-SP03:** Verify Special:Statistics loads with statistics table
- **TC-SP04:** Verify Special:WantedPages loads with wanted pages list
- **TC-SP05:** WCAG accessibility compliance scan of Recent Changes page with axe-core
- **TC-SP06:** Verify Tools menu contains all expected page tools (What links here, Related changes, etc.)
- **TC-SP07:** Verify Appearance options (theme, size, width) can be toggled
- **TC-SP08:** Verify Cite this page tool opens with citation content
- **TC-SP09:** Verify Get shortened URL generates short URL (w.wiki format)
- **TC-SP10:** Verify appearance settings cycle through all options (Small, Large, Wide, Dark, Light)
- **TC-SP11:** Verify appearance settings persist across page navigation

<br>

**Total Test Cases: 51**

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
│       │        ├── ArticleModuleTest.java (6 tests)
│       │        ├── BaseTest.java
│       │        ├── CategoryModuleTest.java (5 tests)
│       │        ├── HistoryModuleTest.java (5 tests)
│       │        ├── HomePageModuleTest.java (5 tests)
│       │        ├── LoginModuleTest.java (5 tests)
│       │        ├── NavigationModuleTest.java (6 tests)
│       │        ├── SearchModuleTest.java (5 tests)
│       │        └── SpecialPagesModuleTest.java (11 tests)
│       │        
│       └── resources
│           └── testng.xml
├── target
│   ├── clasess
│   
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
