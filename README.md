# QKart Enterprise Test Automation Framework

Enterprise-grade, data-driven, self-healing UI automation framework designed for the QKart e‑commerce platform. This framework follows a layered architecture, supports stable parallel execution, provides rich reporting, and ensures long-term maintainability.

---

## 1. Problem Statement

UI test automation often faces challenges such as:

* Flaky tests caused by DOM re-renders
* Browser conflicts during parallel execution
* Hard-coded test data
* Weak reporting without screenshots
* Slow execution time

**This framework solves these issues through:**

* Thread-safe WebDriver using `ThreadLocal`
* Self-healing retry logic for stale elements
* Excel-driven test data
* ExtentReports HTML reports with embedded screenshots
* Configurable environment & browser setup

---

## 2. High-Level Architecture

### Layered Architecture

```
Data Layer
├── Dataset.xlsx
├── config.properties
└── StringConstants

Core Engine
├── DriverFactory
├── ThreadLocal Driver Manager
├── TestNG Listeners
├── Retry Analyzer (Auto-injected)
└── ExtentReports 5 HTML Reporter

Business Logic Layer
├── Test Classes
├── Page Objects
├── Locator Repository
└── Smart Wait Handler
```

### Architecture Summary

* **Data Layer:** Central storage for config and test data
* **Core Engine:** Browser management, reporting, retry, listeners
* **Business Logic:** Page Objects and Tests (clean, readable, scalable)

---

## 3. Technical Highlights

### 3.1 Thread-Safe Parallel Execution

Each test thread receives its own WebDriver instance via `ThreadLocal`, preventing cross-thread interference.

**Benefits:**

* True parallel execution
* No shared-driver issues
* No accidental session closures

---

### 3.2 Self-Healing Stale Element Handling

Modern apps re-render DOM frequently. To avoid flaky failures, the framework wraps Selenium actions with:

* Retry mechanism
* Element re-location
* Smart waiting strategy

This significantly reduces flaky test results.

---

### 3.3 Data-Driven Test Execution

Using Apache POI, the framework reads test scenarios from `Dataset.xlsx`.

**Advantages:**

* Add scenarios without code changes
* Easy maintenance
* Flexible test variations

---

### 3.4 Rich Reporting

ExtentReports generates a single HTML file containing:

* Screenshots (Base64 embedded)
* Execution logs
* Test status summary

This makes reports portable and easy to share.

---

## 4. End-to-End Execution Flow

```
TestNG → Read Excel Data
      → BeforeMethod → Driver Setup (ThreadLocal)
      → Execute Test → Page Object Methods
      → Smart Wait / Retry on failures
      → Capture screenshot on failure
      → Log results to Extent Report
      → AfterMethod → Quit WebDriver
```

---

## 5. Project Structure

```
src/test/java/com/qkart
├── constants
├── driver
├── enums
├── listeners
├── pages
├── reports
├── tests
└── utils

src/test/resources
├── config.properties
├── log4j2.xml
└── Dataset.xlsx
```

---

## 6. Execution Commands

### Parallel Execution

```
mvn clean test -DsuiteXmlFile=testng_parallel.xml
```

### Sequential Execution

```
mvn clean test -DsuiteXmlFile=testng_sequential.xml
```

### From IntelliJ

Right‑click the desired TestNG XML → **Run**.

---

## 7. Configuration

```
url=https://crio-qkart-frontend-qa.vercel.app
browser=chrome
headless=false
implicitWait=15
```

---

## 8. Author

**Natarajan M** – SDET
