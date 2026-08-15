package com.qa.api.client;

import com.qa.api.model.Pet;
import com.qa.api.model.PetStatus;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class PetClient {

    private final RequestSpecification spec;

    public PetClient() {
        this(RequestSpecs.json());
    }

    public PetClient(RequestSpecification spec) {
        this.spec = spec;
    }

    public Response create(Pet pet) {
        return given()
                .spec(spec)
                .body(pet)
                .post("/pet");
    }

    public Response update(Pet pet) {
        return given()
                .spec(spec)
                .body(pet)
                .put("/pet");
    }

    public Response getById(long petId) {
        return given()
                .spec(spec)
                .pathParam("petId", petId)
                .get("/pet/{petId}");
    }

    public Response delete(long petId) {
        return given()
                .spec(spec)
                .pathParam("petId", petId)
                .delete("/pet/{petId}");
    }

    public Response findByStatus(PetStatus status) {
        return given()
                .spec(spec)
                .queryParam("status", status.value())
                .get("/pet/findByStatus");
    }
}
