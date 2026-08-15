package com.qa.api.data;

import com.qa.api.model.Pet;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PetFactoryTest {

    @Test
    void validPetAssignsAUniquePositiveId() {
        Pet first = PetFactory.validPet();
        Pet second = PetFactory.validPet();

        assertThat(first.getId()).isNotNull().isPositive();
        assertThat(second.getId()).isNotNull().isPositive().isNotEqualTo(first.getId());
    }
}
