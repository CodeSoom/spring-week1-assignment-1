package com.codesoom.assignment.enums;

/**
 * https://datatracker.ietf.org/doc/html/rfc7231#section-4.3
 * */
public enum HttpMethod {

    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE");

    private final String method;

    HttpMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
