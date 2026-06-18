package com.tfp.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.tfp.example.domain.PaymentCard;
import io.qameta.allure.Step;

public class PaymentPage {

    private final Page page;
    private final Locator subHeader;
    private final Locator membershipSummary;
    private final Locator amountSummary;
    private final Locator submitButton;
    private final Locator cardholderField;
    private final Locator cardNumberField;
    private final Locator expiryField;
    private final Locator cvcField;

    public PaymentPage(Page page) {
        this.page = page;
        this.subHeader = page.locator(".lede");
        this.membershipSummary = page.getByTestId("payment-membership");
        this.amountSummary = page.getByTestId("payment-amount");
        this.submitButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Pay").setExact(false));
        this.cardholderField = page.getByLabel("Cardholder name");
        this.cardNumberField = page.getByLabel("Card number");
        this.expiryField = page.getByLabel("Expiry (MM/YY)");
        this.cvcField = page.getByLabel("CVC");
    }

    public Locator getSubHeader() {
        return subHeader;
    }

    public Locator membershipSummary() {
        return membershipSummary;
    }

    public Locator amountSummary() {
        return amountSummary;
    }

    public Locator submitButton() {
        return submitButton;
    }

    @Step("Fill in card details for cardholder: {card.cardholderName}")
    public PaymentPage fillCardDetails(PaymentCard card) {
        cardholderField.fill(card.cardholderName());
        cardNumberField.fill(card.cardNumber());
        expiryField.fill(card.expiry());
        cvcField.fill(card.cvc());
        return this;
    }

    @Step("Submit payment")
    public void submitPayment() {
        submitButton.click();
        page.waitForLoadState();
    }
}
