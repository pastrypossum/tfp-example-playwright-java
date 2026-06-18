package com.tfp.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.tfp.example.domain.Member;
import io.qameta.allure.Step;

public class MemberDetailsPage {

    private final Page page;
    private final Locator firstNameField;
    private final Locator lastNameField;
    private final Locator emailField;
    private final Locator phoneField;
    private final Locator dobField;
    private final Locator addressLine1Field;
    private final Locator addressLine2Field;
    private final Locator cityField;
    private final Locator postcodeField;
    private final Locator countrySelect;
    private final Locator continueButton;

    public MemberDetailsPage(Page page) {
        this.page = page;
        this.firstNameField    = page.getByLabel("First name");
        this.lastNameField     = page.getByLabel("Last name");
        this.emailField        = page.getByLabel("Email address");
        this.phoneField        = page.getByLabel("Phone number");
        this.dobField          = page.getByLabel("Date of birth");
        this.addressLine1Field = page.getByLabel("Address line 1");
        this.addressLine2Field = page.getByLabel("Address line 2 (optional)");
        this.cityField         = page.getByLabel("Town / city");
        this.postcodeField     = page.getByLabel("Postcode / ZIP");
        this.countrySelect     = page.getByLabel("Country");
        this.continueButton    = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue"));
    }

    @Step("Fill in player details for {member.firstName} {member.lastName}")
    public MemberDetailsPage fillDetails(Member member) {
        firstNameField.fill(member.firstName());
        lastNameField.fill(member.lastName());
        emailField.fill(member.email());
        phoneField.fill(member.phone());
        dobField.fill(member.dateOfBirth());
        addressLine1Field.fill(member.addressLine1());
        if (member.addressLine2() != null && !member.addressLine2().isBlank()) {
            addressLine2Field.fill(member.addressLine2());
        }
        cityField.fill(member.city());
        postcodeField.fill(member.postcode());
        countrySelect.selectOption(member.country());
        return this;
    }

    @Step("Continue from player details")
    public void clickContinue() {
        continueButton.click();
        page.waitForLoadState();
    }
}
