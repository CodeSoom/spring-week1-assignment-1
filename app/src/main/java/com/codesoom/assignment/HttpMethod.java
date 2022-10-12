package com.codesoom.assignment;

/**
 * @see https://www.rfc-editor.org/rfc/rfc7231
 */
public enum HttpMethod {
    GET("GET"), POST("POST"), PUT("PUT"), PATCH("PATCH"), DELETE("DELETE");

    final String name;

    HttpMethod(String name) {
        this.name = name;
    }

}
