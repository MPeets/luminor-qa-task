package com.qa;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FrameworkSmokeTest {

    @Test
    void junitAndAssertjAreWired() {
        assertThat(1 + 1).isEqualTo(2);
    }
}
