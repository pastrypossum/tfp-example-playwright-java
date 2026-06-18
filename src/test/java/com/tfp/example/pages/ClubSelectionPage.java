package com.tfp.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;

public class ClubSelectionPage {

    private final Page page;
    private final Locator continueButton;

    public ClubSelectionPage(Page page) {
        this.page = page;
        this.continueButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue"));
    }

    @Step("Select club: {clubName}")
    public ClubSelectionPage selectClub(String clubName) {
        page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName(clubName).setExact(false)).click();
        return this;
    }

    @Step("Continue from club selection")
    public void clickContinue() {
        continueButton.click();
        page.waitForLoadState();
    }
}
