package com.tfp.example.tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.tfp.example.domain.ConfirmationSummary;
import com.tfp.example.fixtures.PlaywrightRunner;
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

@Feature("Membership sign-up process")
@DisplayName("E2E user journey for membership sign-up")
public class SignUpTests extends PlaywrightRunner {

    @ParameterizedTest
    @Story("Join club on a single payment scheme")
    @DisplayName("Single payment scheme")
    @CsvSource({
        "Riverside Rovers FC, 'London, United Kingdom', £45.00"})
//        "Pennine Harriers AC, 'Manchester, United Kingdom', £35.00",
//        "Polisportiva Lago di Como, 'Como, Italy', €50.00",
//        "Atletica Verona, 'Verona, Italy', €40.00"})
    public void joinWithOneOffPayment(String club, String address, String payment) {

        clubSelectionPage.selectClub(club).clickContinue();
        memberDetailsPage.fillDetails(ROBERT.getInfo()).clickContinue();

        PlaywrightAssertions.assertThat(membershipTypePage.oneOffOption()).isVisible();
        membershipTypePage.selectOneOff().clickContinue();

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
            "Riverside Rovers FC, 'London, United Kingdom', £6.50"})
//            "Pennine Harriers AC, 'Manchester, United Kingdom', £5.00",
//            "Polisportiva Lago di Como, 'Como, Italy', €7.00",
//            "Atletica Verona, 'Verona, Italy', €6.00"})
    public void joinsWithMonthlyPayment(String club, String address, String payment) {

        String monthlyPayment = payment + " / month";

        clubSelectionPage.selectClub(club).clickContinue();
        memberDetailsPage.fillDetails(ROBERT.getInfo()).clickContinue();

        PlaywrightAssertions.assertThat(membershipTypePage.monthlyOption()).isVisible();
        membershipTypePage.selectMonthly().clickContinue();

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