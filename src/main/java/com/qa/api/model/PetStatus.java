package com.qa.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PetStatus {
    @JsonProperty("available")
    AVAILABLE,
    @JsonProperty("pending")
    PENDING,
    @JsonProperty("sold")
    SOLD
}
