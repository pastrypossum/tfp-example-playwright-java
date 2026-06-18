package com.tfp.example.tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.tfp.example.domain.ConfirmationSummary;
import com.tfp.example.pages.*;
import io.qameta.allure.*;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.tfp.example.domain.PaymentCardTestData.ACCEPTED_TEST_CARD;
import static com.tfp.example.domain.MemberTestData.ROBERT;

@Feature("E2E sign up")
@DisplayName("E2E sign up")
@Execution(ExecutionMode.SAME_THREAD)
public class SignUpTests {

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

    @ParameterizedTest
    @Story("Join club on a single payment scheme")
    @DisplayName("Single payment scheme")
    @CsvSource({
        "Riverside Rovers FC, 'London, United Kingdom', £45.00",
        "Pennine Harriers AC, 'Manchester, United Kingdom', £35.00",
        "Polisportiva Lago di Como, 'Como, Italy', €50.00",
        "Atletica Verona, 'Verona, Italy', €40.00"})
    public void joinWithOneOffPayment(String club, String address, String payment) {

        clubPage.selectClub(club).clickContinue();
        detailsPage.fillDetails(ROBERT.getInfo()).clickContinue();

        PlaywrightAssertions.assertThat(membershipPage.oneOffOption()).isVisible();
        membershipPage.selectOneOff().clickContinueToPayment();

        PlaywrightAssertions.assertThat(paymentPage.getSubHeader()).containsText(club);
        PlaywrightAssertions.assertThat(paymentPage.membershipSummary()).hasText("One-off");
        PlaywrightAssertions.assertThat(paymentPage.amountSummary()).hasText(payment);
        PlaywrightAssertions.assertThat(paymentPage.submitButton()).containsText("Pay " + payment);
        paymentPage.fillCardDetails(ACCEPTED_TEST_CARD.getInfo()).submitPayment();

        ConfirmationSummary summary = confirmationPage.getSummary();
        PlaywrightAssertions.assertThat(confirmationPage.confirmationBanner()).isVisible();
        verifyMembershipPurchase(summary, club, address, payment,"One-off");
    }

    @ParameterizedTest
    @Story("Join club on a monthly payment scheme")
    @DisplayName("Monthly payment scheme")
    @CsvSource({
            "Riverside Rovers FC, 'London, United Kingdom', £6.50",
            "Pennine Harriers AC, 'Manchester, United Kingdom', £5.00",
            "Polisportiva Lago di Como, 'Como, Italy', €7.00",
            "Atletica Verona, 'Verona, Italy', €6.00"})
    public void joinsWithMonthlyPayment(String club, String address, String payment) {

        String monthlyPayment = payment + " / month";

        clubPage.selectClub(club).clickContinue();
        detailsPage.fillDetails(ROBERT.getInfo()).clickContinue();

        PlaywrightAssertions.assertThat(membershipPage.monthlyOption()).isVisible();
        membershipPage.selectMonthly().clickContinueToPayment();

        PlaywrightAssertions.assertThat(paymentPage.getSubHeader()).containsText(club);
        PlaywrightAssertions.assertThat(paymentPage.membershipSummary()).hasText("Monthly recurring");
        PlaywrightAssertions.assertThat(paymentPage.amountSummary()).hasText(monthlyPayment);
        PlaywrightAssertions.assertThat(paymentPage.submitButton()).containsText("Pay " + payment);
        paymentPage.fillCardDetails(ACCEPTED_TEST_CARD.getInfo()).submitPayment();

        ConfirmationSummary summary = confirmationPage.getSummary();
        PlaywrightAssertions.assertThat(confirmationPage.confirmationBanner()).isVisible();
        verifyMembershipPurchase(summary, club, address, monthlyPayment ,"Monthly recurring");
    }

    @Step("Verify membership of {1} for {3}")
    public void verifyMembershipPurchase(ConfirmationSummary summary, String club, String address, String payment, String type) {

        SoftAssertions softly  = new SoftAssertions();
        softly.assertThat(summary.reference()).contains("TFP-000001");
        softly.assertThat(summary.club()).isEqualTo(club + " — " + address);
        softly.assertThat(summary.name()).isEqualTo(ROBERT.getInfo().fullName());
        softly.assertThat(summary.phone()).isEqualTo(ROBERT.getInfo().phone());
        softly.assertThat(summary.email()).isEqualTo(ROBERT.getInfo().email());
        softly.assertThat(summary.membershipType()).isEqualTo(type);
        softly.assertThat(summary.fee()).contains(payment);

        String cardNumber = ACCEPTED_TEST_CARD.getInfo().cardNumber();
        int cardLength = cardNumber.length();
        softly.assertThat(summary.card()).isEqualTo("•••• •••• •••• "
                + cardNumber.substring(cardLength-4,cardLength));

        softly.assertThat(summary.paymentStatus()).isEqualTo("Succeeded");

        softly.assertAll();
    }
}