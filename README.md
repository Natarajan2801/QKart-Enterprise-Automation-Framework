<p align="center">
  <img src="https://img.shields.io/badge/Selenium-4.16.1-43B02A?style=for-the-badge&logo=selenium&logoColor=white" />
  <img src="https://img.shields.io/badge/Java-11+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/TestNG-7.8.0-FF7300?style=for-the-badge&logo=testng&logoColor=white" />
  <img src="https://img.shields.io/badge/Maven-3.6+-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" />
  <img src="https://img.shields.io/badge/Build-Passing-brightgreen?style=for-the-badge" />
</p>

<h1 align="center">🛒 QKart Test Automation Framework</h1>

<p align="center">
  <strong>A Production-Ready, Scalable Selenium Test Automation Framework</strong>
</p>

<p align="center">
  <a href="#-key-features">Features</a> •
  <a href="#-quick-start">Quick Start</a> •
  <a href="#-architecture">Architecture</a> •
  <a href="#-running-tests">Running Tests</a> •
  <a href="#-reports">Reports</a>
</p>

---

## 🎯 Overview

This framework automates end-to-end testing for the **QKart e-commerce application** using industry best practices. Built with **Selenium WebDriver**, **TestNG**, and **Page Object Model**, it provides a robust foundation for scalable test automation.

---

## 🔄 Framework Flow Diagram

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                           TEST EXECUTION FLOW                                    │
└─────────────────────────────────────────────────────────────────────────────────┘

  ┌──────────┐     ┌──────────────┐     ┌─────────────┐     ┌──────────────┐
  │  Maven   │────▶│   TestNG     │────▶│  BaseTest   │────▶│  Test Class  │
  │  (CLI)   │     │  (Runner)    │     │  (Setup)    │     │  (Execute)   │
  └──────────┘     └──────────────┘     └─────────────┘     └──────────────┘
                          │                    │                    │
                          ▼                    ▼                    ▼
                   ┌──────────────┐     ┌─────────────┐     ┌──────────────┐
                   │  Listeners   │     │   Driver    │     │    Page      │
                   │  (Events)    │     │  Factory    │     │   Objects    │
                   └──────────────┘     └─────────────┘     └──────────────┘
                          │                    │                    │
                          ▼                    ▼                    ▼
                   ┌──────────────┐     ┌─────────────┐     ┌──────────────┐
                   │   Extent     │     │  WebDriver  │     │   Actions    │
                   │   Report     │     │  (Browser)  │     │  (Click/Type)│
                   └──────────────┘     └─────────────┘     └──────────────┘
                          │                    │                    │
                          └────────────────────┴────────────────────┘
                                              │
                                              ▼
                                    ┌──────────────────┐
                                    │   TEST RESULTS   │
                                    │  ✅ Pass / ❌ Fail │
                                    └──────────────────┘
```

---

## 🏗️ Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                              FRAMEWORK ARCHITECTURE                              │
└─────────────────────────────────────────────────────────────────────────────────┘

                              ┌───────────────────┐
                              │    TEST LAYER     │
                              │  ┌─────────────┐  │
                              │  │ BaseTest    │  │
                              │  │ TestClasses │  │
                              │  └─────────────┘  │
                              └─────────┬─────────┘
                                        │
                    ┌───────────────────┼───────────────────┐
                    │                   │                   │
                    ▼                   ▼                   ▼
        ┌───────────────────┐ ┌───────────────────┐ ┌───────────────────┐
        │    PAGE LAYER     │ │   CONFIG LAYER    │ │  UTILITY LAYER    │
        │  ┌─────────────┐  │ │  ┌─────────────┐  │ │  ┌─────────────┐  │
        │  │ BasePage    │  │ │  │ ConfigMgr   │  │ │  │ WaitUtils   │  │
        │  │ HomePage    │  │ │  │ Constants   │  │ │  │ ExcelUtils  │  │
        │  │ LoginPage   │  │ │  │ Locators    │  │ │  │ Screenshot  │  │
        │  │ CheckoutPg  │  │ │  └─────────────┘  │ │  └─────────────┘  │
        │  └─────────────┘  │ └───────────────────┘ └───────────────────┘
        └─────────┬─────────┘
                  │
                  ▼
        ┌───────────────────┐
        │   DRIVER LAYER    │
        │  ┌─────────────┐  │
        │  │DriverFactory│  │
        │  │OptionsMgr   │  │
        │  └─────────────┘  │
        └─────────┬─────────┘
                  │
                  ▼
        ┌───────────────────┐
        │  BROWSER LAYER    │
        │  Chrome/Firefox   │
        │      Edge         │
        └───────────────────┘
```

---

## 📊 Test Execution Lifecycle

```
┌────────────────────────────────────────────────────────────────────────────────┐
│                          TEST EXECUTION LIFECYCLE                               │
└────────────────────────────────────────────────────────────────────────────────┘

    ┌─────────┐    ┌─────────┐    ┌─────────┐    ┌─────────┐    ┌─────────┐
    │ @Before │    │  TEST   │    │  PAGE   │    │ ASSERT  │    │ @After  │
    │ Method  │───▶│  START  │───▶│ ACTIONS │───▶│ VERIFY  │───▶│ Method  │
    └─────────┘    └─────────┘    └─────────┘    └─────────┘    └─────────┘
         │              │              │              │              │
         ▼              ▼              ▼              ▼              ▼
    ┌─────────┐    ┌─────────┐    ┌─────────┐    ┌─────────┐    ┌─────────┐
    │ Launch  │    │  Load   │    │ Click   │    │  Pass   │    │  Quit   │
    │ Browser │    │  Page   │    │  Type   │    │  Fail   │    │ Browser │
    │ Config  │    │  Data   │    │ Select  │    │  Skip   │    │ Report  │
    └─────────┘    └─────────┘    └─────────┘    └─────────┘    └─────────┘

    ════════════════════════════════════════════════════════════════════════
    ║  On Failure: Screenshot Captured → Attached to Report → Retry Test   ║
    ════════════════════════════════════════════════════════════════════════
```

---

## ✨ Key Features

| Feature | Description | Benefit |
|---------|-------------|---------|
| 🚀 **Parallel Execution** | Thread-safe WebDriver with ThreadLocal | ⚡ Faster test runs |
| 📊 **Rich Reporting** | ExtentReports with screenshots | 📈 Better visibility |
| 🔄 **Auto Retry** | RetryAnalyzer for flaky tests | 🛡️ Stable results |
| 📝 **Data-Driven** | Excel + Dynamic DataProvider | 🔧 Easy maintenance |
| ⏱️ **Smart Waits** | Zero Thread.sleep() | ✅ Reliable tests |
| 🌐 **Multi-Browser** | Chrome, Firefox, Edge | 🔀 Cross-browser |

---

## 🚀 Quick Start

### Prerequisites
```
✅ Java 11 or higher
✅ Maven 3.6+
✅ Chrome/Firefox/Edge browser
✅ IDE (IntelliJ IDEA / Eclipse) - Optional
```

---

## 💻 How to Run Locally

### Step 1️⃣: Install Prerequisites

```
┌─────────────────────────────────────────────────────────────────┐
│                    INSTALL PREREQUISITES                         │
└─────────────────────────────────────────────────────────────────┘

  📥 JAVA 11+
  └── Download: https://adoptium.net/
  └── Verify:   java -version

  📥 MAVEN 3.6+
  └── Download: https://maven.apache.org/download.cgi
  └── Verify:   mvn -version

  📥 CHROME BROWSER (Latest)
  └── Download: https://www.google.com/chrome/
```

### Step 2️⃣: Clone or Download Project

```bash
# Option A: Clone with Git
git clone https://github.com/Natarajan2801/QKart-Enterprise-Automation-Framework.git
cd qkart-automation

# Option B: Download ZIP and extract
# Then open terminal in the extracted folder
```

### Step 3️⃣: Run Tests

```
┌─────────────────────────────────────────────────────────────────┐
│                      RUN FROM TERMINAL                           │
└─────────────────────────────────────────────────────────────────┘

  # Run all tests (browser will open)
  mvn clean test

  # Run in headless mode (no browser window)
  mvn clean test -Dheadless=true

  # Run specific test
  mvn clean test -Dtest=QkartSanityTests#testSearchFunctionality
```

```
┌─────────────────────────────────────────────────────────────────┐
│                      RUN FROM IDE                                │
└─────────────────────────────────────────────────────────────────┘

  📂 IntelliJ IDEA:
  │
  ├── 1. File → Open → Select project folder
  ├── 2. Wait for Maven to download dependencies
  ├── 3. Right-click on testng_parallel.xml
  └── 4. Click "Run"

  📂 Eclipse:
  │
  ├── 1. File → Import → Maven → Existing Maven Projects
  ├── 2. Select project folder → Finish
  ├── 3. Right-click on testng_parallel.xml
  └── 4. Run As → TestNG Suite
```

### Step 4️⃣: View Results

```
  After test execution:

  📊 Open Report:
  └── reports/QKart_Execution_Report_<timestamp>.html
      (Double-click to open in browser)

  📝 View Logs:
  └── logs/automation.log
```

---

### 🔧 Troubleshooting

| Issue | Solution |
|-------|----------|
| `java: command not found` | Install Java & set JAVA_HOME |
| `mvn: command not found` | Install Maven & add to PATH |
| Chrome not launching | Update Chrome to latest version |
| Tests failing | Check internet connection (app needs it) |
| Port already in use | Close other Chrome instances |

---

## 📁 Project Structure

```
📦 Q-KART/
│
├── 📄 pom.xml                    # Maven dependencies
├── 📄 testng_parallel.xml        # Parallel execution config
├── 📄 testng_sequential.xml      # Sequential execution config
│
├── 📂 src/test/
│   ├── 📂 java/com/qkart/
│   │   │
│   │   ├── 📂 config/            # ⚙️ CONFIGURATION
│   │   │   └── ConfigManager     #    Load & manage properties
│   │   │
│   │   ├── 📂 constants/         # 📍 CONSTANTS
│   │   │   ├── FrameworkConst    #    File paths
│   │   │   ├── LocatorRepo       #    All locators (By)
│   │   │   └── StringConst       #    URL endpoints
│   │   │
│   │   ├── 📂 driver/            # 🚗 DRIVER MANAGEMENT
│   │   │   ├── DriverFactory     #    Create browser instance
│   │   │   └── OptionsManager    #    Browser options
│   │   │
│   │   ├── 📂 enums/             # 🏷️ ENUMERATIONS
│   │   │   ├── BrowserType       #    CHROME, FIREFOX, EDGE
│   │   │   └── WaitStrategy      #    CLICKABLE, VISIBLE, PRESENCE
│   │   │
│   │   ├── 📂 exceptions/        # ❌ CUSTOM EXCEPTIONS
│   │   │   ├── FrameworkExc      #    Base exception
│   │   │   ├── ElementNotFound   #    Element timeout
│   │   │   ├── ConfigExc         #    Config errors
│   │   │   └── BrowserInitExc    #    Driver failures
│   │   │
│   │   ├── 📂 listeners/         # 👂 TEST LISTENERS
│   │   │   ├── TestListener      #    Capture pass/fail/skip
│   │   │   ├── RetryAnalyzer     #    Retry failed tests
│   │   │   └── AnnotationTrans   #    Apply retry globally
│   │   │
│   │   ├── 📂 pages/             # 📄 PAGE OBJECTS
│   │   │   ├── BasePage          #    Common methods
│   │   │   ├── HomePage          #    Home page actions
│   │   │   ├── LoginPage         #    Login actions
│   │   │   ├── RegisterPage      #    Registration
│   │   │   ├── CheckoutPage      #    Checkout flow
│   │   │   ├── ContactUsPage     #    Contact form
│   │   │   └── SearchResult      #    Search results
│   │   │
│   │   ├── 📂 reports/           # 📊 REPORTING
│   │   │   └── ExtentManager     #    Initialize reports
│   │   │
│   │   ├── 📂 tests/             # 🧪 TEST CLASSES
│   │   │   ├── BaseTest          #    Setup & teardown
│   │   │   └── QkartSanityTests  #    Test cases
│   │   │
│   │   └── 📂 utils/             # 🔧 UTILITIES
│   │       ├── DynamicXpath      #    Build dynamic locators
│   │       ├── ExcelUtils        #    Read test data
│   │       ├── ScreenshotUtils   #    Capture screenshots
│   │       └── WaitUtils         #    Smart wait methods
│   │
│   └── 📂 resources/
│       ├── config.properties     # App configuration
│       ├── Dataset.xlsx          # Test data
│       └── log4j2.xml            # Logging config
│
├── 📂 reports/                   # Generated HTML reports
└── 📂 logs/                      # Execution logs
```

---

## ⚙️ Configuration

```properties
# 📄 src/test/resources/config.properties

# 🌐 Application URL
url=https://crio-qkart-frontend-qa.vercel.app

# 🖥️ Browser Settings
browser=chrome          # chrome | firefox | edge
headless=false          # true for CI/CD

# ⏱️ Timeouts (seconds)
implicitWait=10
explicitWait=15
pageLoadTimeout=30

# 🔧 Test Settings
retryCount=1
screenshotOnFailure=true
highlightElements=false
```

---

## 🏃 Running Tests

```
┌─────────────────────────────────────────────────────────────────┐
│                     EXECUTION OPTIONS                            │
└─────────────────────────────────────────────────────────────────┘

  ┌─────────────────────────────────────────────────────────────┐
  │  mvn clean test                    # Default (Parallel)     │
  ├─────────────────────────────────────────────────────────────┤
  │  mvn clean test -Psequential       # Sequential Mode        │
  ├─────────────────────────────────────────────────────────────┤
  │  mvn clean test -Dheadless=true    # Headless Mode          │
  ├─────────────────────────────────────────────────────────────┤
  │  mvn clean test -Dbrowser=firefox  # Firefox Browser        │
  ├─────────────────────────────────────────────────────────────┤
  │  mvn clean test -Dbrowser=edge     # Edge Browser           │
  └─────────────────────────────────────────────────────────────┘

  Run Specific Test:
  ┌─────────────────────────────────────────────────────────────┐
  │  mvn test -Dtest=QkartSanityTests#testHappyFlow            │
  └─────────────────────────────────────────────────────────────┘
```

---

## 📊 Reports & Logs

```
┌─────────────────────────────────────────────────────────────────┐
│                      OUTPUT LOCATIONS                            │
└─────────────────────────────────────────────────────────────────┘

  📈 EXTENT REPORTS
  └── reports/QKart_Execution_Report_<timestamp>.html
      │
      ├── ✅ Test Results Summary
      ├── 📸 Screenshots on Failure
      ├── ⏱️ Execution Time
      └── 📋 Test Details

  📝 LOG FILES
  └── logs/
      ├── automation.log           # All logs
      └── automation-errors.log    # Errors only
```

---

## 🧪 Test Example

```java
@Test(description = "Verify end-to-end purchase flow")
public void testHappyFlow() {
    // 1️⃣ Register new user
    new RegisterPage(getDriver())
        .navigateToRegisterPage()
        .registerUser("testUser", "password123", true);
    
    // 2️⃣ Login & Add to Cart
    new LoginPage(getDriver())
        .performLogin(username, password)
        .navigateToHome()
        .searchForProduct("YONEX")
        .addProductToCart("YONEX");
    
    // 3️⃣ Checkout
    CheckoutPage checkout = homePage.clickCheckout()
        .addNewAddress("123 Test Street")
        .selectAddress("123 Test Street")
        .placeOrder();
    
    // 4️⃣ Verify
    Assert.assertTrue(checkout.verifyOrderPlacedSuccessfully());
}
```

---

## 📂 Design Patterns Used

```
┌─────────────────────────────────────────────────────────────────┐
│                      DESIGN PATTERNS                             │
└─────────────────────────────────────────────────────────────────┘

  ┌──────────────────┐
  │ PAGE OBJECT      │──▶ Each page = separate class with locators
  │ MODEL (POM)      │    & methods (HomePage, LoginPage, etc.)
  └──────────────────┘

  ┌──────────────────┐
  │ FACTORY          │──▶ DriverFactory creates browser instances
  │ PATTERN          │    based on configuration
  └──────────────────┘

  ┌──────────────────┐
  │ SINGLETON        │──▶ ConfigManager - single instance
  │ PATTERN          │    for configuration access
  └──────────────────┘

  ┌──────────────────┐
  │ FLUENT           │──▶ Method chaining for readable code
  │ INTERFACE        │    page.click().type().submit()
  └──────────────────┘

  ┌──────────────────┐
  │ STRATEGY         │──▶ WaitStrategy enum - different wait
  │ PATTERN          │    types (CLICKABLE, VISIBLE, PRESENCE)
  └──────────────────┘
```

---

## 📦 Tech Stack

<p align="center">
  <img src="https://img.shields.io/badge/Selenium-43B02A?style=for-the-badge&logo=selenium&logoColor=white" />
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/TestNG-FF7300?style=for-the-badge&logo=testng&logoColor=white" />
  <img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" />
  <img src="https://img.shields.io/badge/Log4j2-D22128?style=for-the-badge&logo=apache&logoColor=white" />
</p>

| Technology | Version | Purpose |
|:----------:|:-------:|:--------|
| **Selenium** | 4.16.1 | 🌐 Browser automation |
| **TestNG** | 7.8.0 | 🧪 Test framework |
| **ExtentReports** | 5.1.1 | 📊 HTML reporting |
| **WebDriverManager** | 5.6.3 | 🚗 Driver management |
| **Apache POI** | 5.2.3 | 📑 Excel handling |
| **Log4j2** | 2.20.0 | 📝 Logging |

---

## 👨‍💻 Author

<p align="center">
  <strong>Natarajan M</strong>
</p>

<p align="center">
  <a href="https://linkedin.com/in/natraj5">
    <img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" />
  </a>
  <a href="https://github.com/Natarajan2801">
    <img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" />
  </a>
</p>

---

<p align="center">
  <strong>⭐ Star this repository if you find it helpful!</strong>
</p>

<p align="center">
  Made with ❤️ and ☕
</p>
