package com.codesoom.assignment.enums;

/**
 * @see https://www.rfc-editor.org/rfc/rfc7231
 */
public enum HttpMethodType {
    GET("GET"), POST("POST"), PUT("PUT"), PATCH("PATCH"), DELETE("DELETE"),
    ;
    private String method;

    HttpMethodType(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
