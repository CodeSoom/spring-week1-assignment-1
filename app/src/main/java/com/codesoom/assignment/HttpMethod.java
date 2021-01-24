package com.codesoom.assignment;

/**
 * http methods
 */
public enum HttpMethod {
    GET("GET"),
    HEAD("HEAD"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE");

    private String rawString;

    HttpMethod(String rawString) {
        this.rawString = rawString;
    }
}
