package com.qa.api;

import com.qa.api.data.PetFactory;
import com.qa.api.model.Pet;
import com.qa.api.model.PetStatus;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PetSanityTest extends BaseApiTest {

    @Test
    void createPet() {
        Pet pet = PetFactory.validPet();
        track(pet.getId());

        step("Create pet " + pet.getId());
        Response response = petClient.create(pet);

        assertStatus(response, 200);
        assertThat(response.as(Pet.class))
                .extracting(Pet::getId, Pet::getName, Pet::getStatus)
                .containsExactly(pet.getId(), pet.getName(), pet.getStatus());
    }

    @Test
    void retrieveCreatedPet() {
        Pet pet = PetFactory.validPet();
        track(pet.getId());

        step("Create pet " + pet.getId());
        assertStatus(petClient.create(pet), 200);

        step("Retrieve pet " + pet.getId());
        Response response = petClient.getById(pet.getId());

        assertStatus(response, 200);
        assertThat(response.as(Pet.class))
                .extracting(Pet::getId, Pet::getName, Pet::getStatus)
                .containsExactly(pet.getId(), pet.getName(), pet.getStatus());
    }

    @Test
    void updatePetDetails() {
        Pet pet = PetFactory.validPet();
        track(pet.getId());

        step("Create pet " + pet.getId());
        assertStatus(petClient.create(pet), 200);

        pet.setName("qa-pet-updated-" + pet.getId());
        pet.setStatus(PetStatus.SOLD);

        step("Update pet " + pet.getId() + " to name=" + pet.getName() + " status=sold");
        Response updateResponse = petClient.update(pet);
        assertStatus(updateResponse, 200);
        assertThat(updateResponse.as(Pet.class))
                .extracting(Pet::getName, Pet::getStatus)
                .containsExactly(pet.getName(), PetStatus.SOLD);

        step("Retrieve updated pet " + pet.getId());
        Response getResponse = petClient.getById(pet.getId());
        assertStatus(getResponse, 200);
        assertThat(getResponse.as(Pet.class))
                .extracting(Pet::getName, Pet::getStatus)
                .containsExactly(pet.getName(), PetStatus.SOLD);
    }

    @Test
    void deleteCreatedPet() {
        Pet pet = PetFactory.validPet();
        track(pet.getId());

        step("Create pet " + pet.getId());
        assertStatus(petClient.create(pet), 200);

        step("Delete pet " + pet.getId());
        assertStatus(petClient.delete(pet.getId()), 200);

        step("Retrieve deleted pet " + pet.getId() + " (expect 404)");
        assertStatus(petClient.getById(pet.getId()), 404);
    }
}
