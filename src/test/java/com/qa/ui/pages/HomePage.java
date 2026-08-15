package com.qa.ui.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.TimeoutException;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.byRole;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.Wait;
import static com.codeborne.selenide.TextMatchOptions.fullText;

public class HomePage {

    public static final String URL = "https://luminor.lv/en";
    private static final SelenideElement ACCEPT_ALL =
            $(byRole("button", "ACCEPT ALL", fullText().caseInsensitive()));

    private final MainMenu menu = new MainMenu();

    public HomePage open() {
        Selenide.open(URL);
        return acceptCookiesIfPresent();
    }

    public HomePage acceptCookiesIfPresent() {
        try {
            Wait().withTimeout(Duration.ofSeconds(10)).until(driver -> {
                if (ACCEPT_ALL.exists() && ACCEPT_ALL.isDisplayed()) {
                    ACCEPT_ALL.click();
                    return true;
                }
                return false;
            });
        } catch (TimeoutException ignored) {
            // no cookie banner
        }
        return this;
    }

    public MainMenu menu() {
        return menu;
    }
}
