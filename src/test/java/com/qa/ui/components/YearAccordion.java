package com.qa.ui.components;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.attributeMatching;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byRole;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class YearAccordion {

    private final SelenideElement title;
    private final SelenideElement panel;

    public YearAccordion(String year) {
        this.title = $$(byRole("button", year)).find(visible);
        this.panel = panelFor(title);
    }

    public YearAccordion shouldBeOpen() {
        title.shouldBe(visible);
        title.shouldHave(attribute("aria-expanded", "true"));
        return this;
    }

    public YearAccordion shouldHaveReportLink() {
        panel.$$(byRole("link"))
                .filter(attributeMatching("href", ".*\\.pdf(\\?.*)?"))
                .shouldHave(sizeGreaterThan(0));
        return this;
    }

    private static SelenideElement panelFor(SelenideElement title) {
        return $("#" + title.getAttribute("data-toggle-accordion"));
    }
}
