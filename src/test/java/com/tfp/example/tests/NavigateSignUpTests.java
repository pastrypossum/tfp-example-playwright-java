package com.tfp.example.tests;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import com.tfp.example.fixtures.PlaywrightRunner;
import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.tfp.example.domain.PaymentCardTestData.ACCEPTED_TEST_CARD;
import static com.tfp.example.domain.PaymentCardTestData.EMPTY_TEST_CARD;
import static com.tfp.example.domain.MemberTestData.RUTH;
import static com.tfp.example.domain.MemberTestData.EMPTY;

@Feature("Navigate sign-up stages")
@DisplayName("Navigate through stages of the sign-up process")
public class NavigateSignUpTests extends PlaywrightRunner {

    @Nested
    @Story("Navigate forward to the next stage")
    public class NavigateForward {

        @Test
        @DisplayName("Navigate forward to member details")
        public void forwardToMemberDetails() {

            clubSelectionPage.selectClub("Riverside Rovers FC").clickContinue();

            Allure.step("Verify that default member details loaded and progress bar updated", () -> {

                Assertions.assertThat(memberDetailsPage.readDetails()).isEqualTo(EMPTY.getInfo());
                Assertions.assertThat(progressPage.getCurrentStage()).isEqualTo("Your details");
            });
        }

        @Test
        @DisplayName("Navigate forward to membership type")
        public void forwardToMembershipType() {

            clubSelectionPage.selectClub("Riverside Rovers FC").clickContinue();
            memberDetailsPage.fillDetails(RUTH.getInfo()).clickContinue();

            Allure.step("Verify that default membership type loaded and progress bar updated", () -> {

                PlaywrightAssertions.assertThat(membershipTypePage.monthlyOption()).isChecked();
                Assertions.assertThat(progressPage.getCurrentStage()).isEqualTo("Membership");

            });
        }

        @Test
        @DisplayName("Navigate forward to payment")
        public void forwardToPayment() {

            clubSelectionPage.selectClub("Riverside Rovers FC").clickContinue();
            memberDetailsPage.fillDetails(RUTH.getInfo()).clickContinue();
            membershipTypePage.selectMonthly().clickContinue();

            Allure.step("Verify that default payment loaded and progress bar updated", () -> {

                Assertions.assertThat(paymentPage.readDetails()).isEqualTo(EMPTY_TEST_CARD.getInfo());
                Assertions.assertThat(progressPage.getCurrentStage()).isEqualTo("Payment");
            });
        }

        @Test
        @DisplayName("Navigate forward to confirmation")
        public void forwardToConfirmation() {

            clubSelectionPage.selectClub("Riverside Rovers FC").clickContinue();
            memberDetailsPage.fillDetails(RUTH.getInfo()).clickContinue();
            membershipTypePage.selectMonthly().clickContinue();
            paymentPage.fillCardDetails(ACCEPTED_TEST_CARD.getInfo()).submitPayment();

            Allure.step("Verify that confirmation loaded and progress bar updated", () -> {

                PlaywrightAssertions.assertThat(confirmationPage.confirmationBanner()).isVisible();
                Assertions.assertThat(progressPage.getCurrentStage()).isEqualTo("Done");
            });
        }
    }

    @Nested
    @Story("Navigate back to the previous stage")
    public class NavigateBack {

        @Test
        @DisplayName("Cannot navigate back from club selection")
        public void backFromClubSelection() {

            Allure.step("Verify that club selection has no back button", () -> {
                PlaywrightAssertions.assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Back"))).isHidden();
            });
        }

        @Test
        @DisplayName("Navigate back to club selection")
        public void backToClubSelection() {

            clubSelectionPage.selectClub("Pennine Harriers AC").clickContinue();
            memberDetailsPage.clickBack();

            Allure.step("Verify club selection is retained and progress bar updated", () -> {
                Assertions.assertThat(progressPage.getCurrentStage()).isEqualTo("Club");
                PlaywrightAssertions.assertThat(page.getByLabel("Pennine Harriers AC")).isChecked();
            });
        }

        @Test
        @DisplayName("Navigate back to member details")
        public void backToMemberDetails() {

            clubSelectionPage.selectClub("Pennine Harriers AC").clickContinue();
            memberDetailsPage.fillDetails(RUTH.getInfo()).clickContinue();
            membershipTypePage.clickBack();

            Allure.step("Verify member details are retained and progress bar updated", () -> {
                Assertions.assertThat(progressPage.getCurrentStage()).isEqualTo("Your details");
                Assertions.assertThat(memberDetailsPage.readDetails()).isEqualTo(RUTH.getInfo());
            });
        }

        @Test
        @DisplayName("Navigate back to membership type")
        public void backToMembershipType() {

            clubSelectionPage.selectClub("Pennine Harriers AC").clickContinue();
            memberDetailsPage.fillDetails(RUTH.getInfo()).clickContinue();
            membershipTypePage.selectMonthly().clickContinue();
            paymentPage.clickBack();

            Allure.step("Verify membership type is retained and progress bar updated", () -> {
                Assertions.assertThat(progressPage.getCurrentStage()).isEqualTo("Membership");
                PlaywrightAssertions.assertThat(membershipTypePage.monthlyOption()).isChecked();
            });
        }

        @Test
        @DisplayName("Cannot navigate back from confirmation summary")
        public void backFromConfirmationSummary() {

            Allure.step("Verify that confirmation summary has no back button", () -> {
                PlaywrightAssertions.assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Back"))).isHidden();
            });
        }
    }
}
