package com.tfp.example.tests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import com.tfp.example.pages.*;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static com.tfp.example.domain.PaymentCardTestData.DECLINED_TEST_CARD;
import static com.tfp.example.domain.MemberTestData.ROBERT;

@Feature("Card payment")
@DisplayName("Card payment")
@Execution(ExecutionMode.SAME_THREAD)
public class CardPaymentTests {

    private static final String APP_URL = "https://jonathan-riddell.assessment.teamfeepay.dev/s/TjbCZQg7iXx9dDNsufnYHwy8";

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    private ClubSelectionPage clubPage;
    private MemberDetailsPage detailsPage;
    private MembershipTypePage membershipPage;
    private PaymentPage paymentPage;
    private ConfirmationPage confirmationPage;

    @BeforeEach
    public void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        context = browser.newContext();
        page = context.newPage();

        page.navigate(APP_URL);
        page.waitForLoadState();

        clubPage = new ClubSelectionPage(page);
        detailsPage = new MemberDetailsPage(page);
        membershipPage = new MembershipTypePage(page);
        paymentPage = new PaymentPage(page);
        confirmationPage = new ConfirmationPage(page);
    }

    @AfterEach
    public void tearDown() {
        if (context != null) context.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    @Test
    @Story("Stop payment on bank error")
    @DisplayName("Card declined")
    public void cardDeclined() {

        clubPage.selectClub("Riverside Rovers FC").clickContinue();
        detailsPage.fillDetails(ROBERT.getInfo()).clickContinue();
        membershipPage.selectMonthly().clickContinueToPayment();
        paymentPage.fillCardDetails(DECLINED_TEST_CARD.getInfo()).submitPayment();

        PlaywrightAssertions.assertThat(page.getByRole(AriaRole.ALERT))
                .containsText("Your card was declined (card_declined). Please check the details and try again.");
    }
}
