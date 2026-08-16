package com.qa.ui.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Assumptions;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;

import java.time.Duration;
import java.util.Locale;

import static com.codeborne.selenide.Selectors.byRole;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.Wait;
import static com.codeborne.selenide.TextMatchOptions.fullText;

public class HomePage {

    public static final String URL = "https://luminor.lv/en";
    private static final SelenideElement ACCEPT_ALL =
            $(byRole("button", "ACCEPT ALL", fullText().caseInsensitive()));

    private final MainMenu menu = new MainMenu();

    public HomePage open() {
        try {
            Selenide.open(URL);
            Wait().withTimeout(Duration.ofSeconds(15))
                    .ignoring(WebDriverException.class)
                    .until(driver -> siteReady() && !cloudflareChallenge());
        } catch (TimeoutException e) {
            abortIfCloudflare(e);
        } catch (WebDriverException e) {
            abortIfCloudflare(e);
        }
        return acceptCookiesIfPresent();
    }

    private static void abortIfCloudflare(RuntimeException e) {
        if (cloudflareChallenge()) {
            Assumptions.abort(
                    "luminor.lv served a Cloudflare challenge - run uiTest locally"
            );
        }
        throw e;
    }

    private static boolean siteReady() {
        return ACCEPT_ALL.exists()
                || !$$(byRole("button", "Site menu")).isEmpty();
    }

    private static boolean cloudflareChallenge() {
        String html = Selenide.webdriver().driver().source().toLowerCase(Locale.ROOT);
        return html.contains("cf-turnstile")
                || html.contains("challenges.cloudflare.com")
                || html.contains("cdn-cgi/challenge")
                || html.contains("_cf_chl");
    }

    public HomePage acceptCookiesIfPresent() {
        try {
            Wait().withTimeout(Duration.ofSeconds(10))
                    .ignoring(WebDriverException.class)
                    .until(driver -> ACCEPT_ALL.exists() && ACCEPT_ALL.isDisplayed());
            ACCEPT_ALL.click();
        } catch (TimeoutException ignored) {
            // no cookie banner
        }
        return this;
    }

    public MainMenu menu() {
        return menu;
    }
}
