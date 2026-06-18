# Copilot Instructions

## Project Overview

Java 21 acceptance test suite using Playwright for browser automation, JUnit 5 as the test runner, AssertJ for assertions, and Allure for reporting.

## Application Under Test
https://jonathan-riddell.assessment.teamfeepay.dev/s/TjbCZQg7iXx9dDNsufnYHwy8

## Build and Test Commands

```bash
# Run all acceptance tests (surefire is disabled; failsafe runs the tests)
mvn verify

# Run a single test class
mvn verify -Dit.test=SignUpTests

# Run a single test method
mvn verify -Dit.test=SignUpTests#joinWithOneOffPayment

# Generate and open Allure report (after mvn verify)
mvn allure:report
.allure/allure-2.34.0/bin/allure serve target/allure-results
```

## Architecture

- Tests: `src/test/java/com/tfp/example/tests/`
- Page objects: `src/test/java/com/tfp/example/pages/`
- Domain records & test data: `src/test/java/com/tfp/example/domain/`
- Fixtures: `src/test/java/com/tfp/example/fixtures/`
- Test class names must match `**/*Tests.java` to be picked up by Failsafe
- Each test class independently manages the full Playwright lifecycle: `Playwright → Browser → BrowserContext → Page` created in `@BeforeEach`, closed in `@AfterEach`
- Allure AspectJ weaver is injected via `-javaagent` in Failsafe's `argLine` — required for Allure annotations to work

## Key Workflow

1) Examine the feature, list of example scenarios and any guidance provided.

2) Explore **only related** UI pages using the Playwright MCP tools.
   These are **UI tests first** — explore the full user journey through the UI.
   - Use targeted `browser_evaluate` calls to discover locators — do **not** use `browser_snapshot` (too token-heavy).
   - For forms, always explore the **post-save state** — what success/error feedback appears and how the user navigates onward. This determines the correct action method flow.

3) Review the acceptance criteria and suggest additional test scenarios for comprehensive coverage.

4) Write a plan to `user-stories/{feature}/{story}.plan.md`:
   - Include test data for each scenario and where it will be stored (static vs dynamic)
   - Include any page objects that need to be created or updated

5) Present the plan before proceeding with implementation.

6) Do not change example test details to make a test pass — ask for the correct value first.

7) Implement one test at a time:
   - Create any necessary test data as static values in `domain/` enums.
   - Create or update page objects using results from earlier exploration.
   - Run the test immediately after implementing it to prove it works.
   - Fix any failures before moving to the next test.
   - NEVER implement multiple tests without running each one first.
   - Present the final version of each test for review.

## Key Conventions

### Test Data
- Domain objects are **records** in the `domain/` package, e.g.:
  ```java
  public record Member(String firstName, String lastName, String email, String phone,
                       String dateOfBirth, String addressLine1, String addressLine2,
                       String city, String postcode, String country) {
      public String fullName() { return firstName + " " + lastName; }
  }
  ```
- Static test data sets are **enums** wrapping records, e.g.:
  ```java
  public enum MemberTestData {
      ROBERT(new Member("Robert", "Smith", ...));
      public Member getInfo() { return member; }
  }
  ```
- Import test data as static: `import static com.tfp.example.domain.MemberTestData.ROBERT;`
- No data cleanup required; test environment auto-resets.

### Assertions
- All assertions must be in the test, not in page objects.
- Use **PlaywrightAssertions** for UI state: `PlaywrightAssertions.assertThat(locator)`
- Use **AssertJ SoftAssertions** for multi-field confirmation checks: `softly.assertThat(...)`
- Shared assertion logic may be extracted into a `@Step`-annotated helper method within the test class.

### Page Objects
- Accept `Page` in constructor and store as an instance field.
- Declare `Locator` fields in the constructor for reuse.
- Methods describe user actions and return `this` for chaining where appropriate.
- Use `@Step("...")` on every public method.
- Selector priority:
  1. `getByRole()` (preferred)
  2. `getByLabel()`
  3. `getByText()` (if semantically correct)
  4. `getByTestId()` (fallback)
  5. CSS selectors
  6. XPath only as last resort

### Allure Annotations
Class-level:
```java
@Feature("E2E sign up")
@DisplayName("E2E sign up")
@Execution(ExecutionMode.SAME_THREAD)
```

Method-level:
```java
@Story("Join club on a single payment scheme")
@DisplayName("Single payment scheme")
```

Page object methods:
```java
@Step("Select club: {clubName}")
public ClubSelectionPage selectClub(String clubName) { ... }
```
