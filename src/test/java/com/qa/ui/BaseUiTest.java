package com.qa.ui;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseUiTest {

    @BeforeAll
    static void configureSelenide() {
        Configuration.browser = "chrome";
        Configuration.headless = Boolean.parseBoolean(
                System.getProperty("selenide.headless", "true")
        );
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 10_000;
        Configuration.pageLoadTimeout = 30_000;
        Configuration.reportsFolder = "build/reports/selenide";
    }
}
