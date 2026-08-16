package com.qa.api.data;

import com.qa.api.model.Category;
import com.qa.api.model.Pet;
import com.qa.api.model.PetStatus;
import com.qa.api.model.Tag;

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
                .category(Category.builder().id(id).name("qa").build())
                .photoUrls(List.of("https://example.com/qa-pet.png"))
                .tags(List.of(Tag.builder().id(id).name("sanity").build()))
                .status(PetStatus.AVAILABLE)
                .build();
    }
}
