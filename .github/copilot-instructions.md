# Copilot Instructions

## Project Overview

Java 21 acceptance test suite using Playwright for browser automation, JUnit 5 as the test runner, AssertJ for assertions, and Allure for reporting.

## Build and Test Commands

```bash
# Run all acceptance tests (required — surefire is disabled, failsafe runs the tests)
mvn verify

# Run a single test class
mvn verify -Dit.test=Sample1AcceptanceTest

# Run a single test method
mvn verify -Dit.test=Sample1AcceptanceTest#testPageTitle

# Generate and open Allure report (after mvn verify)
mvn allure:report
.allure/allure-2.34.0/bin/allure serve target/allure-results
```

> `mvn test` does **not** run any tests — `maven-surefire-plugin` has `skipTests=true`. All tests run via `maven-failsafe-plugin` during the `verify` phase.

## Architecture

- All tests live in `src/test/java/com/tfp/example/tests/`
- Test class names must match `**/*Test.java` to be picked up by Failsafe
- Each test class independently manages its full Playwright lifecycle: `Playwright → Browser → BrowserContext → Page` created in `@BeforeEach`, closed in `@AfterEach`
- Allure AspectJ weaver is injected via `-javaagent` in Failsafe's `argLine` — required for Allure annotations to work

## Key Conventions

### Allure Annotations
Every test class carries class-level Allure metadata:
```java
@Feature("Web Automation")
@Story("Browser Navigation and Verification")
@DisplayName("Human-readable suite name")
@Owner("QA Team")
```

Each `@Test` method uses:
```java
@DisplayName("...")
@Description("...")
@Severity(SeverityLevel.BLOCKER) // or NORMAL, CRITICAL, MINOR, TRIVIAL
```

### Assertions
Use AssertJ fluent assertions with a descriptive `.as(...)` message:
```java
assertThat(value)
    .as("Descriptive failure message")
    .contains("expected");
```

### Playwright Setup Pattern
All test classes follow the same teardown guard pattern — check for null before closing each resource:
```java
@AfterEach
public void tearDown() {
    if (context != null) context.close();
    if (browser != null) browser.close();
    if (playwright != null) playwright.close();
}
```

### Test Data
`net.datafaker` (Datafaker) is on the compile classpath for generating fake test data.
