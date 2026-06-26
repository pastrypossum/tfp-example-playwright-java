package com.tfp.example.fixtures;

import com.microsoft.playwright.*;
import com.tfp.example.pages.*;
import org.junit.jupiter.api.*;

import java.util.Arrays;

public class PlaywrightRunner {

    private static final String BASE_UI_URL = "https://jonathan-riddell.assessment.teamfeepay.dev/s/TjbCZQg7iXx9dDNsufnYHwy8";

    protected static ThreadLocal<Playwright> playwright = ThreadLocal.withInitial(() -> {
        Playwright playwright = Playwright.create();
        playwright.selectors().setTestIdAttribute("data-testid");
        return playwright;
    });

    protected static ThreadLocal<Browser> browser = ThreadLocal.withInitial(() ->
            playwright.get().chromium().launch(
                    new BrowserType.LaunchOptions()
                            .setHeadless(false)
                            .setArgs(Arrays.asList("--no-sandbox", "--start-maximised"))));

    protected BrowserContext context;
    protected Page page;

    protected ProgressPage progressPage;
    protected ClubSelectionPage clubSelectionPage;
    protected MemberDetailsPage memberDetailsPage;
    protected MembershipTypePage membershipTypePage;
    protected PaymentPage paymentPage;
    protected ConfirmationPage confirmationPage;

    @BeforeAll
    public static void beforeAll() {}

    @BeforeEach()
    public void beforeEach() {

        context = browser.get().newContext();
        page = context.newPage();

        progressPage = new ProgressPage(page);
        clubSelectionPage = new ClubSelectionPage(page);
        memberDetailsPage = new MemberDetailsPage(page);
        membershipTypePage = new MembershipTypePage(page);
        paymentPage = new PaymentPage(page);
        confirmationPage = new ConfirmationPage(page);

        page.navigate(BASE_UI_URL);
    }

    @AfterEach
    public void afterEach() {
        context.close();
    }

    @AfterAll
    public static void afterAll() {

        browser.get().close();
        browser.remove();

        playwright.get().close();
        playwright.remove();
    }
}
