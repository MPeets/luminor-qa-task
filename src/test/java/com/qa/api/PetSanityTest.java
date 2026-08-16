package com.qa.api;

import com.qa.api.data.PetFactory;
import com.qa.api.model.ApiMessage;
import com.qa.api.model.Category;
import com.qa.api.model.Pet;
import com.qa.api.model.PetStatus;
import com.qa.api.model.Tag;
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
        // POST echoes the body; retrieveCreatedPet is the read-back check.
        Pet pet = PetFactory.validPet();
        track(pet.getId());

        step("Create pet " + pet.getId());
        Response response = petClient.create(pet);

        assertStatus(response, 200);
        Pet created = response.as(Pet.class);
        assertThat(created)
                .extracting(Pet::getId, Pet::getName, Pet::getStatus)
                .containsExactly(pet.getId(), pet.getName(), pet.getStatus());
        assertThat(created.getCategory())
                .extracting(Category::getId, Category::getName)
                .containsExactly(pet.getCategory().getId(), pet.getCategory().getName());
        assertThat(created.getTags())
                .extracting(Tag::getName)
                .containsExactly("sanity");
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
    @DisplayName("Updated pet is returned by findByStatus sold")
    void updatedPetAppearsInFindBySoldStatus() {
        Pet pet = PetFactory.validPet();
        track(pet.getId());

        step("Create pet " + pet.getId());
        assertStatus(petClient.create(pet), 200);
        awaitPetReadable(pet.getId());

        pet.setStatus(PetStatus.SOLD);
        step("Update pet " + pet.getId() + " to sold");
        assertStatus(petClient.update(pet), 200);
        awaitPetReadable(pet.getId());

        step("Wait until pet " + pet.getId() + " is listed as sold");
        awaitPetListedByStatus(pet.getId(), PetStatus.SOLD);

        step("Find pets by status sold");
        Response response = petClient.findByStatus(PetStatus.SOLD);
        assertStatus(response, 200);
        assertThat(response.as(Pet[].class))
                .extracting(Pet::getId)
                .contains(pet.getId());
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
        Response response = petClient.delete(pet.getId());
        assertStatus(response, 200);
        assertThat(response.as(ApiMessage.class).getMessage()).isNotBlank();

        step("Wait until pet " + pet.getId() + " is gone");
        awaitPetGone(pet.getId());
    }

    @Test
    @DisplayName("Delete of an already-deleted pet returns 404")
    void deleteAlreadyDeletedPetReturns404() {
        Pet pet = PetFactory.validPet();
        track(pet.getId());

        step("Create and delete pet " + pet.getId());
        assertStatus(petClient.create(pet), 200);
        awaitPetReadable(pet.getId());
        assertStatus(petClient.delete(pet.getId()), 200);
        awaitPetGone(pet.getId());

        step("Delete pet " + pet.getId() + " again (expect 404)");
        assertStatus(petClient.delete(pet.getId()), 404);
    }

    @Test
    @DisplayName("POST with a malformed body returns 400")
    void createWithMalformedBodyReturns400() {
        step("Create pet with malformed JSON (expect 400)");
        Response response = petClient.create("{not-json");

        assertStatus(response, 400);
        assertThat(response.as(ApiMessage.class).getMessage()).isEqualTo("bad input");
    }

    @Test
    @DisplayName("GET with a non-numeric id returns 404")
    void getNonNumericIdReturns404() {
        step("Retrieve pet id 'not-a-number' (expect 404)");
        Response response = petClient.getById("not-a-number");

        assertStatus(response, 404);
        // Live body currently names NumberFormatException. That's a leak, not a contract.
    }
}
