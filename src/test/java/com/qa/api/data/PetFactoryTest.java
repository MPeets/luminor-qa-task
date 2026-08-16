package com.qa.api.data;

import com.qa.api.model.Pet;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("api")
@Feature("Petstore")
class PetFactoryTest {

    @Test
    @DisplayName("Factory assigns a unique positive id")
    void validPetAssignsAUniquePositiveId() {
        Pet first = PetFactory.validPet();
        Pet second = PetFactory.validPet();

        assertThat(first.getId()).isNotNull().isPositive();
        assertThat(second.getId()).isNotNull().isPositive().isNotEqualTo(first.getId());
    }
}
