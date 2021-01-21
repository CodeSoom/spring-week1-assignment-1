package com.codesoom.assignment.web.models;

public enum HttpRequestMethod {
    GET,
    POST,
    PUT,
    PATCH,
    DELETE,
    HEAD,
    NONE;

    public static HttpRequestMethod fromString(String methodString) {
        for (HttpRequestMethod method : HttpRequestMethod.values()) {
            if (method.toString().equals(methodString)) {
                return method;
            }
        }
        return NONE;
    }

}
