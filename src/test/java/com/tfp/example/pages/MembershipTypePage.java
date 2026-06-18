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

    public MembershipTypePage(Page page) {
        this.page = page;
        this.oneOffOption    = page.getByLabel("One-off membership", new Page.GetByLabelOptions().setExact(false));
        this.monthlyOption   = page.getByLabel("Monthly recurring",  new Page.GetByLabelOptions().setExact(false));
        this.oneOffPrice     = page.getByTestId("price-one_off");
        this.monthlyPrice    = page.getByTestId("price-monthly");
        this.continueButton  = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue to payment"));
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
    public void clickContinueToPayment() {
        continueButton.click();
        page.waitForLoadState();
    }
}
