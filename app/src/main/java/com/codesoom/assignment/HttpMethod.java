package com.codesoom.assignment;

import java.util.Arrays;
import java.util.function.Predicate;

public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    DELETE("DELETE"),
    PUT("PUT"),
    PATCH("PATCH");

    private final String name;

    HttpMethod(String method) {
        this.name = method;
    }

    public static HttpMethod from(String method) {
        return Arrays.stream(HttpMethod.values())
                .filter(m-> m.name.equalsIgnoreCase(method))
                .findFirst()
                .orElse(GET);
    }
}
