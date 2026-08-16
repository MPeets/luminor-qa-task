package com.qa.api.client;

import com.qa.api.config.ApiConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.params.CoreConnectionPNames;

public final class RequestSpecs {

    private static final int HTTP_TIMEOUT_MS = 10_000;

    private RequestSpecs() {
    }

    public static RequestSpecification json() {
        return new RequestSpecBuilder()
                .setBaseUri(ApiConfig.BASE_URI)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setConfig(RestAssuredConfig.config().httpClient(
                        HttpClientConfig.httpClientConfig()
                                .setParam(ClientPNames.CONN_MANAGER_TIMEOUT, (long) HTTP_TIMEOUT_MS)
                                .setParam(CoreConnectionPNames.CONNECTION_TIMEOUT, HTTP_TIMEOUT_MS)
                                .setParam(CoreConnectionPNames.SO_TIMEOUT, HTTP_TIMEOUT_MS)
                ))
                .build();
    }
}
