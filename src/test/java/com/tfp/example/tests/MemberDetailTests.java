package com.tfp.example.tests;

import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.tfp.example.domain.Member;
import com.tfp.example.domain.MemberTestData;
import com.tfp.example.fixtures.PlaywrightRunner;
import io.qameta.allure.Allure;
import io.qameta.allure.Story;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static com.tfp.example.domain.ClubTestData.RIVERSIDE;
import static com.tfp.example.domain.MemberTestData.EMPTY;
import static com.tfp.example.domain.MemberTestData.MINIMAL;

@Story("Member details")
@DisplayName("Enter personal details of new member")
public class MemberDetailTests extends PlaywrightRunner {

    // Display empty form
    @Test
    @DisplayName("Show default member detail page")
    public void defaultMemberDetailPage() {

        clubSelectionPage.selectClub(RIVERSIDE.getInfo().name()).clickContinue();

        Allure.step("Verify member detail headers", () -> {
            PlaywrightAssertions.assertThat(memberDetailsPage.getMainHeader()).hasText("Your details");
            PlaywrightAssertions.assertThat(memberDetailsPage.getSubHeader()).hasText("Joining " + RIVERSIDE.getInfo().name() + ".");
        });

        Allure.step("Verify member details are blank by default", () -> {
            Assertions.assertThat(memberDetailsPage.readDetails()).isEqualTo(MemberTestData.EMPTY.getInfo());
        });
    }

    // Mandatory fields
    @Test
    @DisplayName("Show error on missing mandatory field")
    public void missingMandatoryFields() {

        clubSelectionPage.selectClub(RIVERSIDE.getInfo().name()).clickContinue();
        memberDetailsPage.fillDetails(EMPTY.getInfo()).clickContinue();
        List<String> errors = memberDetailsPage.getErrorMessages();

        Allure.step("Verify error for missing mandatory fields", () -> {

            Assertions.assertThat(errors.size()).isEqualTo(7);
            SoftAssertions softly = new SoftAssertions();
            softly.assertThat(errors).contains("Enter your first name");
            softly.assertThat(errors).contains("Enter your last name");
            softly.assertThat(errors).contains("Enter a valid email address");
            softly.assertThat(errors).contains("Enter a valid phone number");
            softly.assertThat(errors).contains("Enter your date of birth");
            softly.assertThat(errors).contains("Enter your address");
            softly.assertThat(errors).contains("Select a country");
            softly.assertAll();
        });
    }

    // Optional fields
    @Test
    @DisplayName("Allow member to omit optional fields")
    public void missingOptionalFields() {

        clubSelectionPage.selectClub(RIVERSIDE.getInfo().name()).clickContinue();
        memberDetailsPage.fillDetails(MINIMAL.getInfo()).clickContinue();

        Allure.step("Verify member details accepted without optional fields", () -> {

            Assertions.assertThat(progressPage.getCurrentStage()).isEqualTo("Membership");
        });
    }
}

