package com.tfp.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.tfp.example.domain.ConfirmationSummary;
import io.qameta.allure.Step;

public class ConfirmationPage {

    private final Page page;
    private final Locator confirmationBanner;
    private final Locator reference;
    private final Locator clubSummary;
    private final Locator nameSummary;
    private final Locator emailSummary;
    private final Locator phoneSummary;
    private final Locator membershipSummary;
    private final Locator feeAmount;
    private final Locator cardSummary;
    private final Locator paymentStatus;

    public ConfirmationPage(Page page) {
        this.page = page;

        this.confirmationBanner = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("You're all set 🎉"));
        this.reference         = page.getByTestId("reference");
        this.clubSummary       = page.getByTestId("summary-club");
        this.nameSummary       = page.getByTestId("summary-name");
        this.emailSummary      = page.getByTestId("summary-email");
        this.phoneSummary      = page.getByTestId("summary-phone");
        this.membershipSummary = page.getByTestId("summary-membership");
        this.feeAmount         = page.getByTestId("fee-amount");
        this.cardSummary       = page.getByTestId("summary-card");
        this.paymentStatus     = page.getByTestId("summary-status");
    }

    public Locator confirmationBanner() { return confirmationBanner; }
    public Locator reference()          { return reference; }
    public Locator clubSummary()        { return clubSummary; }
    public Locator nameSummary()        { return nameSummary; }
    public Locator emailSummary()       { return emailSummary; }
    public Locator phoneSummary()       { return phoneSummary; }
    public Locator membershipSummary()  { return membershipSummary; }
    public Locator feeAmount()          { return feeAmount; }
    public Locator cardSummary()        { return cardSummary; }
    public Locator paymentStatus()      { return paymentStatus; }

    @Step("Read confirmation summary from receipt")
    public ConfirmationSummary getSummary() {
        confirmationBanner.waitFor();
        return new ConfirmationSummary(
                reference.textContent().trim(),
                clubSummary.textContent().trim(),
                nameSummary.textContent().trim(),
                emailSummary.textContent().trim(),
                phoneSummary.textContent().trim(),
                membershipSummary.textContent().trim(),
                feeAmount.textContent().trim(),
                cardSummary.textContent().trim(),
                paymentStatus.textContent().trim()
        );
    }
}
