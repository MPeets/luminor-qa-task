package com.qa.ui.pages;

import com.qa.ui.components.YearAccordion;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byRole;
import static com.codeborne.selenide.Selenide.$;

public class FinancialReportsPage {

    public FinancialReportsPage shouldBeDisplayed() {
        $(byRole("heading", "Financial reports")).shouldBe(visible);
        return this;
    }

    public YearAccordion year(String year) {
        return new YearAccordion(year);
    }
}
