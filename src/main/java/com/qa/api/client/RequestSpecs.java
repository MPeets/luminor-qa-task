package com.qa.api.client;

import com.qa.api.config.ApiConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public final class RequestSpecs {

    private RequestSpecs() {
    }

    public static RequestSpecification json() {
        return new RequestSpecBuilder()
                .setBaseUri(ApiConfig.BASE_URI)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }
}
