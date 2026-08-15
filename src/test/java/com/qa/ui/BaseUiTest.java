package com.qa.ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.openqa.selenium.chrome.ChromeOptions;

@Tag("ui")
public abstract class BaseUiTest {

    @BeforeAll
    static void configureSelenide() {
        Configuration.browser = "chrome";
        Configuration.headless = Boolean.parseBoolean(
                System.getProperty("selenide.headless", "true")
        );
        Configuration.browserSize = "1920x1080";
        ChromeOptions chrome = new ChromeOptions();
        chrome.addArguments("--disable-dev-shm-usage", "--no-sandbox", "--window-size=1920,1080");
        Configuration.browserCapabilities = chrome;
        Configuration.timeout = 10_000;
        Configuration.pageLoadTimeout = 30_000;
        Configuration.reportsFolder = "build/reports/selenide";
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(false));
    }

    @AfterAll
    static void closeBrowser() {
        Selenide.closeWebDriver();
    }
}
