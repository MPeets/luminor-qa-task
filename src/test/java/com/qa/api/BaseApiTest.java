package com.qa.api;

import com.qa.api.client.PetClient;
import com.qa.api.client.RequestSpecs;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class BaseApiTest {

    protected final PetClient petClient = new PetClient(
            new RequestSpecBuilder()
                    .addRequestSpecification(RequestSpecs.json())
                    .addFilter(new RequestLoggingFilter())
                    .addFilter(new ResponseLoggingFilter())
                    .build()
    );
    private final List<Long> createdPetIds = new ArrayList<>();

    protected void track(long petId) {
        createdPetIds.add(petId);
    }

    protected static void step(String message) {
        System.out.println();
        System.out.println("=== " + message + " ===");
    }

    protected static void assertStatus(Response response, int statusCode) {
        assertThat(response.statusCode())
                .as(response.asString())
                .isEqualTo(statusCode);
    }

    @AfterEach
    void deleteTrackedPets() {
        if (!createdPetIds.isEmpty()) {
            step("Cleanup pets " + createdPetIds);
        }
        createdPetIds.forEach(petClient::delete);
        createdPetIds.clear();
    }
}
