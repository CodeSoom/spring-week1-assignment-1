package com.codesoom.assignment.enums;

/**
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc7231#section-4.3">RFC 7231 section 4.3</a>
 */
public enum HttpMethod {

    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE");

    private final String method;

    HttpMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
