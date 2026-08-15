package com.qa.ui.components;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byRole;
import static com.codeborne.selenide.Selenide.$$;

public class YearAccordion {

    private final String year;
    private final SelenideElement title;

    public YearAccordion(String year) {
        this.year = year;
        this.title = $$(byRole("button", year)).find(visible);
    }

    public YearAccordion shouldBeOpen() {
        title.shouldBe(visible);
        title.shouldHave(attribute("aria-expanded", "true"));
        return this;
    }

    public YearAccordion shouldHaveReportLink() {
        SelenideElement report = reportLinks().find(visible);
        report.shouldBe(visible);
        report.shouldHave(attribute("href"));
        return this;
    }

    private ElementsCollection reportLinks() {
        return $$(byRole("link")).filter(text(year));
    }
}
