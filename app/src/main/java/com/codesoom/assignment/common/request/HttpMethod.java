package com.codesoom.assignment.common.request;

public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    DELETE("DELETE"),
    PATCH("PATCH"),
    PUT("PUT");
    private final String method;
    HttpMethod(String method){
        this.method=method;
    }

    public String getMethod() {
        return method;
    }
}
