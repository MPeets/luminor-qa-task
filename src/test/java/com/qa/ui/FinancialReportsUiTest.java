package com.qa.ui;

import com.qa.ui.pages.HomePage;
import org.junit.jupiter.api.Test;

class FinancialReportsUiTest extends BaseUiTest {

    @Test
    void year2026SectionIsOpenWithAReportLink() {
        new HomePage()
                .open()
                .menu()
                .open()
                .openAboutUs()
                .openFinancialReports()
                .year("2026")
                .shouldBeOpen()
                .shouldHaveReportLink();
    }
}
