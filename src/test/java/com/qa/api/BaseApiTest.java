package com.qa.api;

import com.qa.api.client.PetClient;
import com.qa.api.client.RequestSpecs;
import com.qa.api.model.Pet;
import io.qameta.allure.Allure;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Tag("api")
public abstract class BaseApiTest {

    private static final Duration CONSISTENCY_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration POLL_INTERVAL = Duration.ofMillis(250);

    protected final PetClient petClient = new PetClient(
            new RequestSpecBuilder()
                    .addRequestSpecification(RequestSpecs.json())
                    .addFilter(new AllureRestAssured())
                    .addFilter(new RequestLoggingFilter())
                    .addFilter(new ResponseLoggingFilter())
                    .build()
    );
    private final PetClient pollClient = new PetClient();
    private final List<Long> createdPetIds = new ArrayList<>();

    protected void track(long petId) {
        createdPetIds.add(petId);
    }

    protected static void step(String message) {
        System.out.println();
        System.out.println("=== " + message + " ===");
        Allure.step(message);
    }

    protected static void assertStatus(Response response, int statusCode) {
        assertThat(response.statusCode())
                .as(response.asString())
                .isEqualTo(statusCode);
    }

    /**
     * Public Petstore is not read-your-writes consistent: POST can return 200
     * and the next GET still 404. Poll until the pet is readable.
     */
    protected Pet awaitPetReadable(long petId) {
        AtomicReference<Pet> readable = new AtomicReference<>();
        await("pet " + petId + " to become readable")
                .atMost(CONSISTENCY_TIMEOUT)
                .pollDelay(Duration.ZERO)
                .pollInterval(POLL_INTERVAL)
                .untilAsserted(() -> {
                    Response response = pollClient.getById(petId);
                    assertThat(response.statusCode()).isEqualTo(200);
                    readable.set(response.as(Pet.class));
                });
        return readable.get();
    }

    /**
     * Same inconsistency after DELETE: GET can still return 200 briefly.
     * Poll until the pet is gone instead of sleeping a fixed time.
     */
    protected void awaitPetGone(long petId) {
        await("pet " + petId + " to be gone")
                .atMost(CONSISTENCY_TIMEOUT)
                .pollDelay(Duration.ZERO)
                .pollInterval(POLL_INTERVAL)
                .untilAsserted(() -> assertThat(pollClient.getById(petId).statusCode()).isEqualTo(404));
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
