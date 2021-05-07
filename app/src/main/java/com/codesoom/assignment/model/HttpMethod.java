package com.codesoom.assignment.model;

public enum HttpMethod {
    GET("GET"), POST("POST"), PUT("PUT"), PATCH("PATCH"),
    DELETE("DELETE");

    private final String methodName;

    HttpMethod(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return this.getMethodName();
    }
}
