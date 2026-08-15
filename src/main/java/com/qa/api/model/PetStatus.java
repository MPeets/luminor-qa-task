package com.qa.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Locale;

public enum PetStatus {
    @JsonProperty("available")
    AVAILABLE,
    @JsonProperty("pending")
    PENDING,
    @JsonProperty("sold")
    SOLD;

    public String value() {
        return name().toLowerCase(Locale.ROOT);
    }
}
