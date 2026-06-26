package com.tfp.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.tfp.example.domain.Progress;

public class ProgressPage {

    private final Page page;
    private final Locator progressBar;

    public ProgressPage(Page page) {
        this.page = page;
        this.progressBar = page.getByTestId("progress");
    }

    public String getCurrentStage() {
        return page.locator("ol[data-testid='progress'] > li[aria-current='step']").innerText();
    }
}
