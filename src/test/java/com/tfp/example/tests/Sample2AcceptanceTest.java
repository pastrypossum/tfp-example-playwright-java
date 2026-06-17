package com.tfp.example.tests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import io.qameta.allure.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Sample acceptance test demonstrating Playwright + AssertJ + Allure setup
 */
@Feature("Web Automation")
@Story("Browser Navigation and Verification")
@DisplayName("Sample Acceptance Tests")
@Owner("QA Team")
public class Sample2AcceptanceTest {

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    @BeforeEach
    public void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    public void tearDown() {
        if (context != null) {
            context.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    @Test
    @DisplayName("Verify page title contains expected text 2")
    @Description("Navigate to example.com and verify the page title contains 'Example' 2")
    @Severity(SeverityLevel.BLOCKER)
    public void testPageTitle() {
        page.navigate("https://example.com");
        String title = page.title();
        assertThat(title)
                .as("Page title should contain 'Example'")
                .contains("Example");
    }

    @Test
    @DisplayName("Verify page content is not empty and contains expected text 2")
    @Description("Navigate to example.com and verify the page content is not empty and contains 'Example Domain' 2")
    @Severity(SeverityLevel.NORMAL)
    public void testPageContent() {
        page.navigate("https://example.com");
        String content = page.content();
        assertThat(content)
                .as("Page content should not be empty")
                .isNotEmpty()
                .contains("Example Domain");
    }
}

