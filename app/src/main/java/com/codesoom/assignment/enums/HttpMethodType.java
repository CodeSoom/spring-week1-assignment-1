package com.codesoom.assignment.enums;

import java.util.Arrays;

/**
 * @see https://www.rfc-editor.org/rfc/rfc7231
 */
public enum HttpMethodType {
    GET("GET"), POST("POST"), PUT("PUT"), PATCH("PATCH"), DELETE("DELETE"), NOT_SUPPORT("NOT_SUPPORT"),
    ;
    private String method;

    HttpMethodType(String method) {
        this.method = method;
    }

    public String getMethodValue() {
        return method;
    }

    public static HttpMethodType getMethod(String method) {
        return Arrays.stream(HttpMethodType.values())
                .filter(methodType -> methodType.getMethodValue().equals(method))
                .findFirst()
                .orElse(HttpMethodType.NOT_SUPPORT);
    }
}
