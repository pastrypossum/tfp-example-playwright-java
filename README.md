# Project Title

TFP technical exercise

## Description

Technical exercise implementing example tests on a dummy membership site.

Tests include the following:
 - User journey to sign up for a membership with monthly payment
 - User journey to sign up for a membership with annual payment
 - One negative test showing handling of card decline error

Test failures are expected due to international issues.

Inclusion of failing tests is intentional to demonstrate working assertions and reporting.

## Getting Started

### Dependencies

### Core Testing Framework
- **Java 21** — Language version
- **Playwright 1.60.0** — Cross-browser browser automation
- **JUnit 5 (Jupiter) 5.14.4** — Test framework and runner
- **AssertJ 3.27.7** — Fluent assertions

### Reporting & Instrumentation
- **Allure 2.34.0** — Test reporting and analytics
- **AspectJ 1.9.25** — Bytecode weaver for Allure annotations

### Build Tools
- **Maven 3.6+** — Build and dependency management
- **Maven Failsafe 3.3.1** — Runs integration tests (acceptance tests)
- **Maven Compiler 3.13.0** — Compiles Java 21 source

Local development will require a global playwright MCP / CLI installation.

Using CLI is preferred for more efficient token usage.


### Installing

Clone from https://github.com/pastrypossum/tfp-example-playwright-java

Execute tests and generate reports (see executing program section below)

### Executing program

Run the tests with
```
mvn clean verify
```

You can open `target/site/allure-maven-plugin/index.html` with intellij and let it serve the report.

To serve reports from your local environment without intellij you will need to run the following command:
```
mvn io.qameta.allure:allure-maven:serve
```

You can combine the above commands to run tests and serve the report in one command:
```
cd tfp-example-playwright-java && mvn clean verify && mvn io.qameta.allure:allure-maven:serve
```

Note: The CI workflow will execute tests, but it does not currently serve the report.


## Help

Automated tests are showing failures due to known issues.

1)  Amount charged per club and membership type is incorrect in all cases expect one (TFP001)
    You will see an error message as follows:
```
[ERROR] com.tfp.example.tests.SignUpTests.joinsWithMonthlyPayment(String, String, String)[1] -- Time elapsed: 7.549 s <<< FAILURE!
org.opentest4j.AssertionFailedError: 
Locator expected to have text
Expected: £6.50 / month
Received: £45.00 / month
```

## Next steps

Reduce boilerplate in test classes by refactoring common setup/teardown code into a base class.
* Refactor creation of playwright objects into a base class
* Move URL into this base class
* Move before each into this base class
* Move after each into this base class

Include a screenshot in after each for failed tests

Improve reporting
* Update CI workflow to store and post the allure report to GitHub Pages
* Include link to latest report in CI workflow summary for easy access

