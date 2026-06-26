package com.tfp.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.tfp.example.domain.Club;
import io.qameta.allure.Step;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClubSelectionPage {

    private final Page page;
    private final Locator mainHeader;
    private final Locator subHeader;
    private final Locator continueButton;

    public ClubSelectionPage(Page page) {
        this.page = page;
        this.mainHeader = page.locator("h1");
        this.subHeader = page.locator(".lede");
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

    public Locator getMainHeader() {
        return mainHeader;
    }

    public Locator getSubHeader() {
        return subHeader;
    }

    // AI
    @Step("Get all available club options")
    public List<String> getAllClubOptions() {
        List<String> clubOptions = new ArrayList<>();

        for (Locator choice : page.locator(".choice").all()) {
            String title = choice.locator(".choice__title").innerText();
            String meta = choice.locator(".choice__meta").innerText();
            clubOptions.add(title + " " + meta);
        }

        return clubOptions;
    }
}
