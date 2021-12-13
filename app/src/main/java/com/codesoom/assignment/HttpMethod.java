package com.codesoom.assignment;

/**
 * Enumerated type for HTTP method
 * written based on RFC2616 section-10
 *
 * @link https://datatracker.ietf.org/doc/html/rfc2616#section-10
 */
public enum HttpMethod {
    
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE");

    private String value;

    HttpMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
