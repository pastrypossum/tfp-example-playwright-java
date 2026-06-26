package com.tfp.example.tests;


import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.tfp.example.domain.Club;
import com.tfp.example.domain.ClubTestData;
import com.tfp.example.fixtures.PlaywrightRunner;
import io.qameta.allure.Allure;
import io.qameta.allure.Story;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.tfp.example.domain.ClubTestData.RIVERSIDE;

@Story("Club selection")
@DisplayName("Selection club to join")
public class ClubSelectionTests extends PlaywrightRunner {

    @Test
    @DisplayName("Show clubs available")
    public void showClubsAvailable() {

        List<String> availbleOptions = clubSelectionPage.getAllClubOptions();

        Integer expectedOptionCount = ClubTestData.values().length;
        Allure.step("Verify there are only " + expectedOptionCount + " available options: ", () -> {
            Assertions.assertThat(availbleOptions.size()).isEqualTo(expectedOptionCount);
        });

        for (ClubTestData c : ClubTestData.values()) {

            Club club = ClubTestData.valueOf(c.toString()).getInfo();
            String expectedClubDetails = club.name() + " " + club.address()
                    + " · One-off " + club.annualCost() + " · Monthly " + club.monthlyCost();

            Allure.step("Verify available options contain for: " + expectedClubDetails, () -> {
                Assertions.assertThat(availbleOptions).contains(expectedClubDetails);
            });
        }
    }

    @Test
    @DisplayName("Show default club selection page")
    public void defaultClubSelection() {

        Allure.step("Verify club selection headers", () -> {
            PlaywrightAssertions.assertThat(clubSelectionPage.getMainHeader()).hasText("Choose your club");
            PlaywrightAssertions.assertThat(clubSelectionPage.getSubHeader()).hasText("Select the club you'd like to join.");
        });

        // Failure there is only 1 session and that session will store last club selected
        Allure.step("Verify first club is selected by default", () -> {
            PlaywrightAssertions.assertThat(page.getByLabel(RIVERSIDE.getInfo().name())).isChecked();
        });
    }
}
