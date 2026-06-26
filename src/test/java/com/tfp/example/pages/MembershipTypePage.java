package com.tfp.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;

public class MembershipTypePage {

    private final Page page;
    private final Locator oneOffOption;
    private final Locator monthlyOption;
    private final Locator oneOffPrice;
    private final Locator monthlyPrice;
    private final Locator continueButton;
    private final Locator backButton;

    public MembershipTypePage(Page page) {
        this.page = page;
        this.oneOffOption    = page.getByLabel("One-off membership", new Page.GetByLabelOptions().setExact(false));
        this.monthlyOption   = page.getByLabel("Monthly recurring",  new Page.GetByLabelOptions().setExact(false));
        this.oneOffPrice     = page.getByTestId("price-one_off");
        this.monthlyPrice    = page.getByTestId("price-monthly");
        this.continueButton  = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue to payment"));
        this.backButton      = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Back"));
    }

    public Locator oneOffOption()  { return oneOffOption; }
    public Locator monthlyOption() { return monthlyOption; }
    public Locator oneOffPrice()   { return oneOffPrice; }
    public Locator monthlyPrice()  { return monthlyPrice; }

    @Step("Select one-off membership plan")
    public MembershipTypePage selectOneOff() {
        oneOffOption.click();
        return this;
    }

    @Step("Select monthly recurring membership plan")
    public MembershipTypePage selectMonthly() {
        monthlyOption.click();
        return this;
    }

    @Step("Continue to payment from membership selection")
    public void clickContinue() {
        continueButton.click();
        page.waitForLoadState();
    }

    @Step("Back to member details")
    public void clickBack() {
        backButton.click();
        page.waitForLoadState();
    }
}
