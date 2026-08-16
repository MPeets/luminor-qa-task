package com.qa.api;

import com.qa.api.data.PetFactory;
import com.qa.api.model.Pet;
import com.qa.api.model.PetStatus;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Feature("Petstore")
class PetSanityTest extends BaseApiTest {

    @Test
    @DisplayName("Create a pet")
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
    @DisplayName("Retrieve a created pet")
    void retrieveCreatedPet() {
        Pet pet = PetFactory.validPet();
        track(pet.getId());

        step("Create pet " + pet.getId());
        assertStatus(petClient.create(pet), 200);

        step("Wait until pet " + pet.getId() + " is readable");
        awaitPetReadable(pet.getId());

        step("Retrieve pet " + pet.getId());
        Response response = petClient.getById(pet.getId());

        assertStatus(response, 200);
        assertThat(response.as(Pet.class))
                .extracting(Pet::getId, Pet::getName, Pet::getStatus)
                .containsExactly(pet.getId(), pet.getName(), pet.getStatus());
    }

    @Test
    @DisplayName("Update pet name and status")
    void updatePetDetails() {
        Pet pet = PetFactory.validPet();
        track(pet.getId());

        step("Create pet " + pet.getId());
        assertStatus(petClient.create(pet), 200);
        awaitPetReadable(pet.getId());

        pet.setName("qa-pet-updated-" + pet.getId());
        pet.setStatus(PetStatus.SOLD);

        step("Update pet " + pet.getId() + " to name=" + pet.getName() + " status=sold");
        Response updateResponse = petClient.update(pet);
        assertStatus(updateResponse, 200);
        assertThat(updateResponse.as(Pet.class))
                .extracting(Pet::getName, Pet::getStatus)
                .containsExactly(pet.getName(), PetStatus.SOLD);

        step("Wait until pet " + pet.getId() + " is readable after update");
        awaitPetReadable(pet.getId());

        step("Retrieve updated pet " + pet.getId());
        Response getResponse = petClient.getById(pet.getId());
        assertStatus(getResponse, 200);
        assertThat(getResponse.as(Pet.class))
                .extracting(Pet::getName, Pet::getStatus)
                .containsExactly(pet.getName(), PetStatus.SOLD);
    }

    @Test
    @DisplayName("Unknown pet id returns 404")
    void unknownPetIdReturns404() {
        long unknownId = PetFactory.newId();

        step("Retrieve unknown pet " + unknownId + " (expect 404)");
        assertStatus(petClient.getById(unknownId), 404);
    }

    @Test
    @DisplayName("Delete a created pet")
    void deleteCreatedPet() {
        Pet pet = PetFactory.validPet();
        track(pet.getId());

        step("Create pet " + pet.getId());
        assertStatus(petClient.create(pet), 200);
        awaitPetReadable(pet.getId());

        step("Delete pet " + pet.getId());
        assertStatus(petClient.delete(pet.getId()), 200);

        step("Wait until pet " + pet.getId() + " is gone");
        awaitPetGone(pet.getId());
    }
}
