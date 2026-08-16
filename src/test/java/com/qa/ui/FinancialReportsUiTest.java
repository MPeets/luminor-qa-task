package com.qa.ui;

import com.qa.ui.pages.HomePage;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Feature("Luminor website")
class FinancialReportsUiTest extends BaseUiTest {

    @Test
    @DisplayName("2026 financial reports section is open with a PDF")
    void year2026SectionIsOpenWithAReportLink() {
        step("Open English home page and go to Financial reports");
        var reports = new HomePage()
                .open()
                .menu()
                .open()
                .openAboutUs()
                .openFinancialReports();

        step("2026 section is open and has a report PDF");
        reports.year("2026")
                .shouldBeOpen()
                .shouldHaveReportLink();
    }
}
