package com.qa.ui.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byRole;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.Wait;

public class MainMenu {

    private final SelenideElement hamburger =
            $$(byRole("button", "Site menu")).find(visible);
    private final SelenideElement aboutUs = $(byRole("button", "About Us"));
    private final SelenideElement financialReports = $(byRole("link", "Financial Reports"));

    public MainMenu open() {
        hamburger.shouldBe(visible).click();
        waitUntilExpandedOrVisible(hamburger, aboutUs);
        return this;
    }

    public MainMenu openAboutUs() {
        aboutUs.shouldBe(visible).click();
        waitUntilExpandedOrVisible(aboutUs, financialReports);
        return this;
    }

    public FinancialReportsPage openFinancialReports() {
        financialReports.shouldBe(visible).click();
        return new FinancialReportsPage().shouldBeDisplayed();
    }

    private static void waitUntilExpandedOrVisible(SelenideElement toggle, SelenideElement content) {
        Wait().until(driver -> expanded(toggle) || content.is(visible));
    }

    private static boolean expanded(SelenideElement toggle) {
        return "true".equalsIgnoreCase(toggle.getAttribute("aria-expanded"));
    }
}
