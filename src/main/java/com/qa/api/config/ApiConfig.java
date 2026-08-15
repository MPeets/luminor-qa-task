package com.qa.api.config;

public final class ApiConfig {

    public static final String BASE_URI = System.getProperty(
            "api.baseUri",
            "https://petstore.swagger.io/v2"
    );

    private ApiConfig() {
    }
}
