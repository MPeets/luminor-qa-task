package com.qa.api.data;

import com.qa.api.model.Pet;
import com.qa.api.model.PetStatus;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Builds pets with IDs chosen on the client.
 * Petstore is a shared sandbox; server-assigned IDs collide with other users' data.
 */
public final class PetFactory {

    private PetFactory() {}

    public static long newId() {
        return ThreadLocalRandom.current().nextLong(1_000_000_000L, Long.MAX_VALUE);
    }

    public static Pet validPet() {
        long id = newId();
        return Pet.builder()
                .id(id)
                .name("qa-pet-" + id)
                .photoUrls(List.of("https://example.com/qa-pet.png"))
                .status(PetStatus.AVAILABLE)
                .build();
    }
}
