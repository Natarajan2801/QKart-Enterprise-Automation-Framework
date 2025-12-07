🛒 QKart Enterprise Automation Framework

📖 Executive Summary

This is an Enterprise-Grade, Data-Driven Automation Framework engineered for the QKart E-commerce platform. It transforms fragile linear scripts into a Self-Healing, Parallel-Ready, and Observable testing solution.

Designed using SOLID principles and industry-standard Design Patterns, this framework solves common automation challenges like flaky tests, stale elements, and race conditions during parallel execution.

🏗 High-Level Architecture

The framework is built on a modular, layered architecture to ensure separation of concerns.

graph TD
subgraph "Data Layer"
Excel[(Dataset.xlsx)]
Config[config.properties]
Constants[StringConstants]
end

    subgraph "Core Engine"
        Factory[DriverFactory]
        TL[ThreadLocal Manager]
        Listener[TestListener]
        Transformer[AnnotationTransformer]
        Report[ExtentReports 5]
    end

    subgraph "Business Logic"
        Tests[Test Classes]
        POM[Page Object Model]
        Locators[Locator Repository]
        Waits[Smart Wait Strategy]
    end

    Excel -->|Feeds Data| Tests
    Tests -->|Invokes| POM
    POM -->|Read| Locators
    Tests -->|Init| Factory
    Factory -->|Wrap Driver| TL
    Listener -->|Watch| Tests
    Transformer -->|Inject| RetryAnalyzer
    Listener -->|Generate| Report


⚡ Deep Dive: Technical Highlights

1. Parallel Execution & Thread Safety

The Challenge: In standard frameworks, a static WebDriver instance causes session conflicts when running tests in parallel (e.g., Thread A closes Thread B's browser).

The Solution:

We utilize ThreadLocal<WebDriver> within BaseTest.

This creates a thread-isolated container for each driver instance.

Result: You can run thread-count="4" in testng.xml, reducing regression time by 75% with zero cross-thread contamination.

2. Self-Healing Mechanics (Resilience)

The Challenge: Modern React/Angular apps frequently re-render the DOM, causing StaleElementReferenceException.

The Solution:

The BasePage wrapper methods (click, sendKeys) implement a Smart Retry Loop.

If a stale element is detected, the framework automatically re-fetches the locator and retries the action up to 3 times before failing.

Result: Flakiness is drastically reduced without hard Thread.sleep waits.

3. Context-Aware Reporting

Extent Reports 5: Generates an interactive HTML dashboard.

Base64 Snapshots: Screenshots are captured only on failure and converted to Base64 strings. They are embedded directly into the HTML file.

Why Base64? It makes the report portable (single file). You can email it to stakeholders, and the images will never appear broken.

4. Dynamic Data Driving

Apache POI Integration: Tests are decoupled from data.

Excel Provider: The ExcelUtils class dynamically reads test scenarios from Dataset.xlsx. Adding a new test scenario is as simple as adding a row to the Excel sheet—no code changes required.

🔄 Execution Sequence Diagram

The following diagram illustrates the lifecycle of a single test case execution flow, demonstrating how the ThreadLocal driver and Smart Waits interact.

sequenceDiagram
participant TestNG as TestNG Engine
participant Data as Excel Provider
participant Base as BaseTest (ThreadLocal)
participant Page as Page Object
participant Driver as WebDriver
participant Report as Extent Report

    TestNG->>Data: Fetch Test Data (Row 1)
    Data-->>TestNG: Return {Username, Password}
    
    TestNG->>Base: @BeforeMethod (setUp)
    Base->>Driver: Initialize ChromeDriver
    Driver-->>Base: Session ID: 12345 (Stored in ThreadLocal)
    
    TestNG->>Page: Run Test(Username, Password)
    Page->>Driver: FindElement (Smart Wait)
    Driver-->>Page: WebElement Found
    Page->>Driver: Click()
    
    alt StaleElementReferenceException
        Page->>Page: Catch & Retry (Self-Healing)
        Page->>Driver: Click() again
    end
    
    Driver-->>Page: Action Success
    
    alt Test Failed?
        TestNG->>Driver: Capture Screenshot (Base64)
        TestNG->>Report: Log Failure + Embed Image
    else Test Passed
        TestNG->>Report: Log PASS
    end
    
    TestNG->>Base: @AfterMethod (tearDown)
    Base->>Driver: Quit Session 12345


📂 Project Structure

src/test/java/com/qkart
├── constants       # Single Source of Truth (Paths, URLs, Locators)
├── driver          # WebDriver Factory & Browser Options Manager
├── enums           # Type-safe WaitStrategies (CLICKABLE, VISIBLE)
├── listeners       # TestNG Listeners (Reporting, Retry Logic injection)
├── pages           # Page Object Classes (Business Logic only)
├── reports         # Generated Timestamped HTML Reports
├── tests           # Test Classes (Assertions & Flows)
└── utils           # Utilities (Excel, Config, Dynamic Xpath)

src/test/resources
├── config.properties  # Global Environment Config
├── log4j2.xml         # Logging Config
└── Dataset.xlsx       # Test Data


⚡ How to Run Tests

The framework supports two execution modes via Maven.

1. High-Speed Parallel Execution

Best for nightly regression suites.

mvn clean test -DsuiteXmlFile=testng_parallel.xml


2. Sequential Execution (Debug Mode)

Best for debugging specific flows or recording execution videos.

mvn clean test -DsuiteXmlFile=testng_sequential.xml


3. IDE Execution

Right-click on either testng_parallel.xml or testng_sequential.xml in IntelliJ and select Run.

🔧 Configuration

Modify src/test/resources/config.properties to control the environment:

url=[https://crio-qkart-frontend-qa.vercel.app](https://crio-qkart-frontend-qa.vercel.app)
browser=chrome         # Options: chrome, firefox, edge
headless=false         # Options: true (for CI), false (for UI)
implicitWait=15        # Global timeout in seconds


👨‍💻 Author

Natarajan M