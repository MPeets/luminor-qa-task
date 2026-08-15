package com.qa.ui.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.TimeoutException;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Selectors.byRole;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.Wait;

public class HomePage {

    public static final String URL = "https://luminor.lv/en";
    private static final List<String> COOKIE_BUTTONS = List.of(
            "I agree",
            "Accept All Cookies",
            "Accept All"
    );

    private final MainMenu menu = new MainMenu();

    public HomePage open() {
        Selenide.open(URL);
        return acceptCookiesIfPresent();
    }

    public HomePage acceptCookiesIfPresent() {
        try {
            Wait().withTimeout(Duration.ofSeconds(5)).until(driver -> {
                for (String name : COOKIE_BUTTONS) {
                    SelenideElement button = $(byRole("button", name));
                    if (button.exists() && button.isDisplayed()) {
                        button.click();
                        return true;
                    }
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
