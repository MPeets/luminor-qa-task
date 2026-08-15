package com.qa.ui.components;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byRole;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.Wait;

public class YearAccordion {

    private final String year;
    private final SelenideElement title;

    public YearAccordion(String year) {
        this.year = year;
        this.title = $$(byRole("button", year)).filter(visible).first();
    }

    public YearAccordion shouldBeOpen() {
        title.shouldBe(visible);
        Wait().until(driver -> expanded() || hasVisibleReportLink());
        return this;
    }

    public YearAccordion shouldHaveReportLink() {
        SelenideElement report = reportLinks().filter(visible).first();
        report.shouldBe(visible);
        report.shouldHave(attribute("href"));
        return this;
    }

    private boolean expanded() {
        return "true".equalsIgnoreCase(title.getAttribute("aria-expanded"));
    }

    private boolean hasVisibleReportLink() {
        return !reportLinks().filter(visible).isEmpty();
    }

    private ElementsCollection reportLinks() {
        return $$(byRole("link")).filter(text(year));
    }
}
