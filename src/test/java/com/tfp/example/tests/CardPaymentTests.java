package com.tfp.example.tests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import com.tfp.example.fixtures.PlaywrightRunner;
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
public class CardPaymentTests extends PlaywrightRunner {

    // Display empty form

    // Validation of card details

    // Mandatory fields

    // Field validation - name
    // Field validation - number
    // Field validation - expiry
    // Field validation - cvc
    // Field validation - expiry



    @Test
    @Story("Stop payment on bank error")
    @DisplayName("Card declined")
    public void cardDeclined() {

        clubSelectionPage.selectClub("Riverside Rovers FC").clickContinue();
        memberDetailsPage.fillDetails(ROBERT.getInfo()).clickContinue();
        membershipTypePage.selectMonthly().clickContinue();
        paymentPage.fillCardDetails(DECLINED_TEST_CARD.getInfo()).submitPayment();

        PlaywrightAssertions.assertThat(page.getByRole(AriaRole.ALERT))
                .containsText("Your card was declined (card_declined). Please check the details and try again.");
    }
}
