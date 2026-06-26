package com.tfp.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.tfp.example.domain.Member;
import io.qameta.allure.Step;

import java.util.List;

public class MemberDetailsPage {

    private final Page page;
    private final Locator mainHeader;
    private final Locator subHeader;
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
    private final Locator countrySelectValue;
    private final Locator continueButton;
    private final Locator backButton;
    private final Locator errors;

    public MemberDetailsPage(Page page) {
        this.page = page;
        this.mainHeader = page.locator("h1");
        this.subHeader = page.locator(".lede");
        this.firstNameField = page.getByLabel("First name");
        this.lastNameField = page.getByLabel("Last name");
        this.emailField = page.getByLabel("Email address");
        this.phoneField = page.getByLabel("Phone number");
        this.dobField = page.getByLabel("Date of birth");
        this.addressLine1Field = page.getByLabel("Address line 1");
        this.addressLine2Field = page.getByLabel("Address line 2 (optional)");
        this.cityField = page.getByLabel("Town / city");
        this.postcodeField = page.getByLabel("Postcode / ZIP");
        this.countrySelect = page.getByLabel("Country");
        this.countrySelectValue = page.locator("select[data-testid='field-country'] > option[selected='']");
        this.continueButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue"));
        this.backButton = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Back"));
        this.errors = page.locator(".field__error");
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

    @Step("Back to club selection")
    public void clickBack() {
        backButton.click();
        page.waitForLoadState();
    }

    public Locator getMainHeader() {
        return mainHeader;
    }

    public Locator getSubHeader() {
        return subHeader;
    }

    public List<String> getErrorMessages() {

        return errors.allInnerTexts();
    }

    @Step("Read member details from the form")
    public Member readDetails() {
        return new Member(
                firstNameField.inputValue(),
                lastNameField.inputValue(),
                emailField.inputValue(),
                phoneField.inputValue(),
                dobField.inputValue(),
                addressLine1Field.inputValue(),
                addressLine2Field.inputValue(),
                cityField.inputValue(),
                postcodeField.inputValue(),
                countrySelectValue.textContent()
        );
    }
}
